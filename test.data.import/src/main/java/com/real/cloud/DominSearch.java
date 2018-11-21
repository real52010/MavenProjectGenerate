package com.real.cloud;

public class DominSearch {
	public static void main(String[] args) {
		String suffix = "znw";
		/**
		 * xxzn
		 */
		// for (int i = 1; i < 10; i++) {
		// for (int j = 1; j < 10; j++) {
		// System.out.println(i + "" + j + suffix);
		// }
		//
		// }
		/**
		 * xxzn aazn
		 */
//		for (int i = 1; i < 10; i++) {
//			for (int j = 1; j < 10; j++) {
//				System.out.println(i + "" + j + suffix);
//			}
//			for (int m = 97; m < 123; m++) {
//				System.out.println(i + "" + (char) m + suffix);
//				// System.out.println((char)m);
//			}
//			// System.out.println(i+""+j+"jc");
//			// }
//		}
		/**
		 * xxzn aazn
		 */
		for (int i = 97; i < 123; i++) {

			for (int m = 97; m < 123; m++) {
				System.out.println((char) i + "" + (char) m + suffix);
				// System.out.println((char)m);
			}
			// System.out.println(i+""+j+"jc");
			// }
		}
//		for (int x = 97; x < 123; x++) {
//			for (int i = 97; i < 123; i++) {
//
//				for (int m = 97; m < 123; m++) {
//					System.out.println((char) x + "" + (char) i + "" + (char) m + suffix);
//					// System.out.println((char)m);
//				}
//				// System.out.println(i+""+j+"jc");
//				// }
//			}
//		}
	}
}
