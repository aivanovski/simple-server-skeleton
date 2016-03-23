package com.simple.server.dao;

import com.simple.server.model.Person;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.orm.hibernate3.HibernateOperations;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PersonRepositoryTest {

	private static final long PERSON_ID = 1L;
	private static final String PERSON_GIVEN_NAME = "Albert";
	private static final String PERSON_FAMILY_NAME = "Einstein";

	private final PersonRepository repository = new PersonRepository();

	@Before
	public void initMocks() {
		repository.hibernate = Mockito.mock(HibernateOperations.class);
	}

	@Test
	public void allPersonsReturnedFromDB() {
		when(repository.hibernate.loadAll(Person.class)).thenReturn(asList(new Person(PERSON_ID, PERSON_GIVEN_NAME, PERSON_FAMILY_NAME)));

		List<Person> persons = repository.getAll();
		assertThat(persons.size(), is(1));

		Person person = persons.get(0);
		assertThat(person.getId(), is(PERSON_ID));
		assertThat(person.getGivenName(), is(PERSON_GIVEN_NAME));
		assertThat(person.getFamilyName(), is(PERSON_FAMILY_NAME));
	}

	@Test
	public void newPersonIsPersistedInDB() {
		repository.add(new Person(0L, PERSON_GIVEN_NAME, PERSON_FAMILY_NAME));

		ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
		verify(repository.hibernate).save(captor.capture());

		Person person = captor.getValue();
		assertThat(person.getGivenName(), is(PERSON_GIVEN_NAME));
		assertThat(person.getFamilyName(), is(PERSON_FAMILY_NAME));
	}

	@Test
	public void personIsDeletedFromDB() {
		when(repository.hibernate.get(Person.class, PERSON_ID)).thenReturn(new Person(PERSON_ID, PERSON_GIVEN_NAME, PERSON_FAMILY_NAME));

		repository.delete(PERSON_ID);

		ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
		verify(repository.hibernate).delete(captor.capture());

		Person person = captor.getValue();
		assertThat(person.getId(), is(PERSON_ID));
		assertThat(person.getGivenName(), is(PERSON_GIVEN_NAME));
		assertThat(person.getFamilyName(), is(PERSON_FAMILY_NAME));
	}

	@Test
	public void personIsUpdatedInDB() {
		repository.update(new Person(PERSON_ID, PERSON_GIVEN_NAME, PERSON_FAMILY_NAME));

		ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
		verify(repository.hibernate).update(captor.capture());

		Person person = captor.getValue();
		assertThat(person.getId(), is(PERSON_ID));
		assertThat(person.getGivenName(), is(PERSON_GIVEN_NAME));
		assertThat(person.getFamilyName(), is(PERSON_FAMILY_NAME));
	}
}
