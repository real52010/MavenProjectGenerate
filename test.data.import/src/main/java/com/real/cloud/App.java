package com.real.cloud;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.neo4j.cypher.internal.compiler.v2_1.docbuilders.internalDocBuilder;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException {

		// "商户编码,商户名称,交易日期及时间,交易类型,交易金额,交易卡号,卡类别,终端编码,商户手续费,检索参考号,订单号\r\n"

		// System.out.println(Pattern.matches(cardPattern, "6225887825795145"));
		// System.out.println(Pattern.matches(feePattern, "111100.00"));
		String startDate = null;
		String endDate = null;
		 startDate = "20180818";
		 endDate = "20180831";
		String billDir = "D:\\tlorder\\billDir";
		String scriptDir = "D:\\tlorder\\scriptDir";
		System.out.println("账单地址:"+billDir);
		System.out.println("脚本地址:"+scriptDir);
		String destDir = null;

		Calendar preWeekSundayCal = Calendar.getInstance();
		preWeekSundayCal.set(Calendar.DAY_OF_WEEK, 1);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");// HH:mm:ss
		endDate = endDate == null ? sdf2.format(preWeekSundayCal.getTime()) : endDate;
		preWeekSundayCal.add(Calendar.DATE, -6);
		startDate = startDate == null ? sdf2.format(preWeekSundayCal.getTime()) : startDate;
		int weekOYear = preWeekSundayCal.get(Calendar.WEEK_OF_YEAR);
		destDir = billDir + "\\" + startDate + "-" + endDate + "(" + weekOYear + ")";

		//
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// HH:mm:ss
		System.out.println("当前时间：" + sdf.format(d));
		String importScriptPath = String.format(scriptDir + "\\tlpos_order_prepaid_%s.sql", sdf.format(d));
		String upgradeScriptPath = String.format(scriptDir + "\\upgrade_pos_card_%s.sql", sdf.format(d));

		File destDirFile = new File(destDir);
		if (!destDirFile.exists()) {
			destDirFile.mkdirs();
		}
		File scriptDirFile = new File(scriptDir);
		if (!scriptDirFile.exists()) {
			scriptDirFile.mkdirs();
		}

		App.download(destDir,startDate,endDate);
		App.createImportOrderData(destDir, importScriptPath);
		App.createUpgradeOrderData(destDir, upgradeScriptPath);
	}

	public static void downloadLastWeek(String destDir) {

		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");// HH:mm:ss
		Calendar preWeekSundayCal = Calendar.getInstance();
		preWeekSundayCal.set(Calendar.DAY_OF_WEEK, 1);
		Calendar endDate = (Calendar) preWeekSundayCal.clone();
		preWeekSundayCal.add(Calendar.DATE, -6);
		Calendar startDate = (Calendar) preWeekSundayCal.clone();
		// System.out.println(sdf2.format(startDate.getTime()));
		// System.out.println(sdf2.format(endDate.getTime()));

		String startDateStr = sdf2.format(startDate.getTime());
		String endDateStr = sdf2.format(endDate.getTime());
		download(destDir, startDateStr, endDateStr);
	}

	public static void download(String destDir, String startDateStr, String endDateStr) {

		String host = "112.95.232.217";// 主机地址
		int port = 9022;// 主机端口
		String username = "huilian";// 服务器用户名
		String password = "4J4k791h";// 服务器密码
		String planPath = "";// 文件所在服务器路径

		Calendar startDate = Calendar.getInstance();

		Calendar endDate = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		SftpClientUtil clientUtil = null;
		try {
			Date date = format.parse(startDateStr);
			startDate.setTime(date);
			System.out.println(startDate.getTime().toLocaleString());
			date = format.parse(endDateStr);
			endDate.setTime(date);
			System.out.println(endDate.getTime().toLocaleString());
			clientUtil = new SftpClientUtil(host, port, username, password);
			clientUtil.connect();
			while (!startDate.after(endDate)) {
				String fileNamePrx = format.format(startDate.getTime());
				String fileName = fileNamePrx + ".txt";
				String dirName = fileNamePrx.substring(0, 6);
				System.out.println(fileName);
				clientUtil.cd("/821440370130085/");
				clientUtil.download(dirName, fileName, destDir);
				startDate.add(Calendar.DATE, 1);
			}

		} catch (ParseException e) {
			throw new RuntimeException("日期转换错误");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (clientUtil != null) {
					clientUtil.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("关闭连接出错");
			}
		}

	}

	private static void createUpgradeOrderData(String srcDir, String scriptPath) throws IOException {
		List<File> list = FileTools.ls(srcDir, true);
		File tarFile = new File(scriptPath);
		if (tarFile.exists()) {
			tarFile.delete();
		}
		tarFile.createNewFile();
		FileReader reader = null;
		BufferedReader br = null;
		FileWriter writer = null;
		BufferedWriter bw = null;

		// update hyf_pos_pay_order set card_no='621700****8547',trans_fee='5.00'
		// ,pay_code='284355889608' where service_type=2 and
		// tl_order_code='917931200629194752';

		try {
			writer = new FileWriter(tarFile, true);
			bw = new BufferedWriter(writer);
			// bw.write("商户编码,商户名称,交易日期及时间,交易类型,交易金额,交易卡号,卡类别,终端编码,商户手续费,检索参考号,订单号\r\n");
			String sqlFormat = "update hyf_pos_pay_order set card_no='%s',trans_fee='%s'	,pay_code='%s'	 where service_type=2 and tl_order_code='%s';";
			String cardPattern = "^[0-9\\*]{12,24}$";
			String feePattern = "^(0|([1-9][0-9]{0,4})|(([0]\\.\\d{1,2}|[1-9][0-9]{0,4}\\.\\d{1,2})))$";
			String referPattern = "^\\d{10,12}$";

			String orderNoPattern = "^\\d{15,40}$";
			for (File file : list) {
				if (file.isFile()) {

					reader = new FileReader(file);
					br = new BufferedReader(reader);

					String line = br.readLine();
					while ((line = br.readLine()) != null) {
						if (!"".equals(line)) {
							String[] cardData = line.split(",");
							if (cardData.length < 11) {
								System.out.println("数据长度不够：" + cardData[2] + ":" + cardData[4]);
								continue;
							}
							String cardNO = cardData[5];
							String transFee = cardData[8];
							String payCode = cardData[9];
							String orderCode = cardData[10];

							if (!Pattern.matches(cardPattern, cardNO)) {
								throw new RuntimeException("卡号格式不正常." + cardNO);
							}
							if (!Pattern.matches(feePattern, transFee)) {
								throw new RuntimeException("手续费格式不正常." + transFee);

							}
							if (!Pattern.matches(referPattern, payCode)) {
								throw new RuntimeException("参考号格式不正常." + payCode);

							}
							if (!Pattern.matches(orderNoPattern, orderCode)) {
								throw new RuntimeException("订单号格式不正常." + orderCode);

							}
							String sql = String.format(sqlFormat, cardNO, transFee, payCode, orderCode);
							bw.write(sql + "\r\n");

						}
					}
					bw.flush();
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	private static void createImportOrderData(String srcDir, String scriptPath) throws IOException {
		String sqlFormat = " INSERT INTO `hyf`.`tlpos_order_precard` (`交易日期`,`商户编码`, `商户名称`, `交易日期及时间`, `交易类型`, `交易金额`, `交易卡号`, `卡类别`, `终端编码`, `商户手续费`, `检索参考号`, `订单号`) "
				+ "VALUES ('%s','%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');";

		File tarFile = new File(scriptPath);
		if (tarFile.exists()) {
			tarFile.delete();
		}
		tarFile.createNewFile();
		List<File> list = FileTools.ls(srcDir, true);
		FileReader reader = null;
		BufferedReader br = null;
		FileWriter writer = null;
		BufferedWriter bw = null;
		String billTime= null;
		try {
			writer = new FileWriter(tarFile, true);
			bw = new BufferedWriter(writer);
			// bw.write("商户编码,商户名称,交易日期及时间,交易类型,交易金额,交易卡号,卡类别,终端编码,商户手续费,检索参考号,订单号\r\n");
			for (File file : list) {
				if (file.isFile()) {

					reader = new FileReader(file);
					br = new BufferedReader(reader);
					 billTime=file.getName().substring(0,file.getName().lastIndexOf("."));
					String line = br.readLine();
					while ((line = br.readLine()) != null) {
						if (!"".equals(line)) {

							String[] cardData = line.split(",");
							if (cardData.length < 11) {
								System.out.println("数据长度不够：" + cardData[2] + ":" + cardData[4]);
								continue;
							}
							String sql = String.format(sqlFormat,billTime, cardData[0], cardData[1], cardData[2], cardData[3],
									cardData[4], cardData[5], cardData[6], cardData[7], cardData[8], cardData[9],
									cardData[10]);
							bw.write(sql + "\r\n");

						}
					}
					bw.flush();
				}
			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}
}
