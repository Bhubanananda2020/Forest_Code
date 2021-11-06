package com.crts.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "requests_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_id")
	private int reqid;

	@NotEmpty(message = "Department Code Required !!")
	@Size(min = 4, max = 6, message = "Department Most Have Minimun Length 4 and maximun 6 charactes are Required !!")
	@Column(length = 25, name = "request_dept")
	private String reqdeptcode;

	@Column(length = 25, name = "request_number")
	private String reqcode;

	@NotEmpty(message = "Request title can't empty!!")
	@Size(min = 4, max = 20, message = "Minimun Length 4 and maximun 20 charactes are Required !!")
	@Column(length = 25, name = "request_title")
	private String reqtitle;

	@NotEmpty(message = "Request Description Can't Empty!!")
	@Size(min = 10, max = 180, message = "Minimun Length 10 and maximun 180 charactes are Required !!")
	@Column(length = 200, name = "request_description")
	private String reqdesc;

	@Column(name = "assigned_to")
	private int reqassignto;

	@Column(name = "assigned_date")
	private Date reqassigndate;

	@Column(length = 255, name = "initial_comments")
	private String reqinicomment;

	@Column(name = "severity")
	private int severity;

	@Column(name = "piority")
	private int piority;

	@Column(name = "created_By")
	private int recreatedby;

	@OneToMany(mappedBy = "requestEntity", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<StatusEntity> statusEntity;

	@OneToMany(mappedBy = "requestEntity", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<CommentsEntity> cCommentsEntity;

}