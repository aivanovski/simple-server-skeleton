package com.simple.server.model;

import javax.persistence.Access;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Access(FIELD)
public class Person {

	@GeneratedValue(strategy = AUTO)
	@Id
	Long id;

	String givenName;
	String familyName;

	Person() {
	}

	public Person(Long id, String givenName, String familyName) {
		this.id = id;
		this.givenName = givenName;
		this.familyName = familyName;
	}

	public Long getId() {
		return id;
	}

	public String getGivenName() {
		return givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
}
