package com.real.beans;

public class MvnProject { 
	String projectDirectory;
	String groupId;
	String artifactId;
	String archetypeArtifactId; 
	String version;
	String javaPackage;
	String archetypeVersion;
	String archetypeGroupId;
	String name;
	String description;
	String serviceGroup;
	MvnProject parent;
	MvnProject children[];

	public String getProjectDirectory() {
		return projectDirectory;
	}

	public void setProjectDirectory(String projectDirectory) {
		this.projectDirectory = projectDirectory;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getArchetypeArtifactId() {
		return archetypeArtifactId;
	}

	public void setArchetypeArtifactId(String archetypeArtifactId) {
		this.archetypeArtifactId = archetypeArtifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getJavaPackage() {
		return javaPackage;
	}

	public void setJavaPackage(String javaPackage) {
		this.javaPackage = javaPackage;
	}

 
	public MvnProject[] getChildren() {
		return children;
	}

	public void setChildren(MvnProject[] children) {
		this.children = children;
	}
	

	public String getArchetypeGroupId() {
		return archetypeGroupId;
	}

	public void setArchetypeGroupId(String archetypeGroupId) {
		this.archetypeGroupId = archetypeGroupId;
	}

	public String getArchetypeVersion() {
		return archetypeVersion;
	}

	public void setArchetypeVersion(String archetypeVersion) {
		this.archetypeVersion = archetypeVersion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MvnProject getParent() {
		return parent;
	}

	public void setParent(MvnProject parent) {
		this.parent = parent;
	}

	public String getServiceGroup() {
		return serviceGroup;
	}

	public void setServiceGroup(String serviceGroup) {
		this.serviceGroup = serviceGroup;
	}
	
	
	
}
