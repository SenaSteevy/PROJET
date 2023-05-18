package com.sivo.resources;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString

@Entity
@Table(name = "TaskDescription")
public class TaskDescription implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	@Id
	@Include
	@Column(name = "id")
	private long id;
	
	@Column(name = "code_order")
	private String codeOrder;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "supplement")
	private String supplement ;
	

	
}
