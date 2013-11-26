// default package
// Generated Jul 31, 2013 2:03:33 PM by Hibernate Tools 3.4.0.CR1
package org.akaza.openclinica.domain.datamap;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.akaza.openclinica.domain.AbstractMutableDomainObject;
import org.akaza.openclinica.domain.DataMapDomainObject;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * StudyGroup generated by hbm2java
 */
@Entity
@Table(name = "study_group")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "study_group_study_group_id_seq") })

public class StudyGroup  extends DataMapDomainObject {

	private int studyGroupId;
	private StudyGroupClass studyGroupClass;
	private String name;
	private String description;
	private Set<SubjectGroupMap> subjectGroupMaps = new HashSet(0);

	public StudyGroup() {
	}

	public StudyGroup(int studyGroupId) {
		this.studyGroupId = studyGroupId;
	}

	public StudyGroup(int studyGroupId, StudyGroupClass studyGroupClass,
			String name, String description, Set subjectGroupMaps) {
		this.studyGroupId = studyGroupId;
		this.studyGroupClass = studyGroupClass;
		this.name = name;
		this.description = description;
		this.subjectGroupMaps = subjectGroupMaps;
	}

	@Id
	@Column(name = "study_group_id", unique = true, nullable = false)
	public int getStudyGroupId() {
		return this.studyGroupId;
	}

	public void setStudyGroupId(int studyGroupId) {
		this.studyGroupId = studyGroupId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_group_class_id")
	public StudyGroupClass getStudyGroupClass() {
		return this.studyGroupClass;
	}

	public void setStudyGroupClass(StudyGroupClass studyGroupClass) {
		this.studyGroupClass = studyGroupClass;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", length = 1000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "studyGroup")
	public Set<SubjectGroupMap> getSubjectGroupMaps() {
		return this.subjectGroupMaps;
	}

	public void setSubjectGroupMaps(Set<SubjectGroupMap> subjectGroupMaps) {
		this.subjectGroupMaps = subjectGroupMaps;
	}

}
