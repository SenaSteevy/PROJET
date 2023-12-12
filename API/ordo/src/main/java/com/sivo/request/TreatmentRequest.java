package com.sivo.request;

import com.sivo.domain.Phase;
import com.sivo.domain.Treatment;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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

public class TreatmentRequest {

	private String description;
	
	private Phase phase ;
}
