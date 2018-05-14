package com.real.generate;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Joiner;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.real.beans.MvnProject;

public class JAVAProjectCreater {
	static String mavenHone = System.getenv("MAVEN_HOME");
	static String classPath = mavenHone + "\\boot\\plexus-classworlds-2.5.2.jar";
	static String classworlds = mavenHone + "\\bin\\m2.conf";

	public static void main(String[] args) throws Exception {

		// List<String> paramList = new LinkedList<String>();
		// String groupId = "com.zyh.maven.quickstart";
		// String artifactId = "simple";
		// String archetypeArtifactId = "maven-archetype-quickstart";
		// String version = "4.0.0-SNAPSHOT";
		// String javaPackage = "com.test.maven.quickstart";
		// String projectDirectory =
		// "D:\\MavenProject\\hyf.microserver\\hyf.basedata.parent";
		//
		// System.setProperty("classworlds.conf", classworlds);
		// System.setProperty("maven.home", mavenHone);
		// System.setProperty("maven.multiModuleProjectDirectory", mavenHone);
		// System.setProperty("user.dir", projectDirectory);
		// System.setProperty("interactiveMode", "false");
		//
		// paramList.add("archetype:generate");
		// paramList.add("-DgroupId=" + groupId);
		// paramList.add("-DartifactId=" + artifactId);
		// paramList.add("-DarchetypeArtifactId=" + archetypeArtifactId);
		// paramList.add("-Dversion=" + version);
		// paramList.add("-Dpackage=" + javaPackage);
		// Launcher.main(paramList.toArray(new String[paramList.size()]));

		// paramList.add("archetype:generate");
		// paramList.add("-DgroupId=me.davenkin");
		// paramList.add("-DartifactId=maven-multi-module");
		// paramList.add("-DarchetypeGroupId=org.codehaus.mojo.archetypes");
		// paramList.add("-DarchetypeArtifactId=pom-root");
		// paramList.add("-DinteractiveMode=false");
		//
		Configuration conf = Configuration.builder().build();
		MvnProject[] array = JsonPath.using(conf).parse(new File("projects.json")).read("$.projects",
				MvnProject[].class);

		MvnProject defaultCfg = JsonPath.parse(new File("projects.json")).read("$.default", MvnProject.class);

		for (MvnProject mvnProject : array) {
			doCreate(null, mvnProject, defaultCfg);
		}

	}

	public static int execute(String workingDirectory, List<String> paramList) throws Exception {

		 try {
		 Runtime rt = Runtime.getRuntime();
		 String command = "cmd /c mvn " + Joiner.on(" ").join(paramList.toArray(new
		 String[paramList.size()]));
		
		 System.out.println("==========================================");
		 System.out.println(command);
		 System.out.println("==========================================");
		 Process pr = rt.exec(command,null,new File(workingDirectory)); // cmd /c calc
		
		 BufferedReader input = new BufferedReader(new
		 InputStreamReader(pr.getInputStream(), "GBK"));
		
		 String line = null;
		
		 while ((line = input.readLine()) != null) {
		 System.out.println(line);
		 }
		
		 int exitVal = pr.waitFor();
		 System.out.println("Exited with error code " + exitVal);
		 return exitVal;
		
		 } catch (Exception e) {
		 System.out.println(e.toString());
		 e.printStackTrace();
		 }
		 return 0;
//		return Launcher.mainWithExitCode(paramList.toArray(new String[paramList.size()]));
		
//		System.out.println("================================================================");
//		System.out.println(Joiner.on(" ").join(paramList.toArray(new String[paramList.size()])));
//		System.out.println("================================================================");
//		
//		
//		
//		 MavenCli cli = new MavenCli();
//		 return cli.doMain(paramList.toArray(new String[paramList.size()]),
//		 workingDirectory, System.out, System.err);

	}

	public static void doCreate(MvnProject parentProject, MvnProject project, MvnProject defaultCfg) throws Exception {
		if (defaultCfg != null) {
			project.setGroupId(project.getGroupId() == null ? defaultCfg.getGroupId() : project.getGroupId());
			project.setVersion(project.getVersion() == null ? defaultCfg.getVersion() : project.getVersion());
			project.setJavaPackage(
					project.getJavaPackage() == null ? defaultCfg.getJavaPackage() : project.getJavaPackage());
			project.setArchetypeGroupId(
					project.getArchetypeGroupId() == null ? defaultCfg.getArchetypeGroupId() : project.getArchetypeGroupId());
			project.setArchetypeVersion(
					project.getArchetypeVersion() == null ? defaultCfg.getArchetypeVersion() : project.getArchetypeVersion());

		}
		if (project.getProjectDirectory() == null) {
			project.setProjectDirectory(
					parentProject != null ? (parentProject.getProjectDirectory() + "/" + parentProject.getArtifactId())
							: null);
		}

		System.setProperty("classworlds.conf", classworlds);
		System.setProperty("maven.home", mavenHone);
		System.setProperty("maven.multiModuleProjectDirectory", mavenHone);
		System.setProperty("user.dir", project.getProjectDirectory());
		System.setProperty("interactiveMode", "false");
		List<String> paramList = new LinkedList<String>();

		paramList.add("archetype:generate");
		paramList.add("-DgroupId=" + project.getGroupId());
		paramList.add("-DartifactId=" + project.getArtifactId());
		paramList.add("-DarchetypeArtifactId=" + project.getArchetypeArtifactId());
		if(project.getArchetypeArtifactId().startsWith("hyf")) {
			paramList.add("-DarchetypeVersion="+project.getArchetypeVersion());
			paramList.add("-DarchetypeGroupId="+project.getArchetypeGroupId());
			
		}
		
		paramList.add("-Dversion=" + project.getVersion());
		paramList.add("-Dpackage=" + project.getJavaPackage());

		paramList.add("-DinteractiveMode=false");

		if ("pom-root".equals(project.getArchetypeArtifactId())) {
			paramList.add("-DarchetypeGroupId=org.codehaus.mojo.archetypes");
		} else {
			paramList.add("-X");
			paramList.add("-B");
		}
		// paramList.add("archetype:generate");
		// paramList.add("-DgroupId=" + groupId);
		// paramList.add("-DartifactId=" + artifactId);
		// paramList.add("-DarchetypeArtifactId=" + archetypeArtifactId);
		// paramList.add("-Dversion=" + version);
		// paramList.add("-Dpackage=" + javaPackage);

		execute(project.getProjectDirectory(), paramList);
		// int extCode = Launcher.mainWithExitCode(paramList.toArray(new
		// String[paramList.size()]));
		// String command=Joiner.on(" ").join(paramList.toArray(new
		// String[paramList.size()]));
		// execute("mvn "+command);
		// System.out.println("---------------------------------------------------------------"
		// + extCode);
		MvnProject[] children = project.getChildren();
		if (children != null) {
			for (MvnProject mvnProject : children) {
				doCreate(project, mvnProject, defaultCfg);
			}

		}

	}
}
