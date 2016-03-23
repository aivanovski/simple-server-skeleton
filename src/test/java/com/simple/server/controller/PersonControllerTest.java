package com.simple.server.controller;

import com.simple.server.dao.PersonRepository;
import com.simple.server.model.Person;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

public class PersonControllerTest {

	private static final long PERSON_ID = 1L;
	private static final String PERSON_GIVEN_NAME = "Albert";
	private static final String PERSON_FAMILY_NAME = "Einstein";

	private final PersonController controller = new PersonController();

	@Before
	public void initMocks() {
		controller.repository = Mockito.mock(PersonRepository.class);
	}

	@Test
	public void newPersonIsPersisted() {
		controller.add(new Person(0L, PERSON_GIVEN_NAME, PERSON_FAMILY_NAME));

		ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
		verify(controller.repository).add(captor.capture());

		Person person = captor.getValue();
		assertThat(person.getGivenName(), is(PERSON_GIVEN_NAME));
		assertThat(person.getFamilyName(), is(PERSON_FAMILY_NAME));
	}

	@Test
	public void personIsDeleted() {
		controller.delete(PERSON_ID);

		ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
		verify(controller.repository).delete(captor.capture());

		Long id = captor.getValue();
		assertThat(id, is(PERSON_ID));
	}

	@Test
	public void personIsUpdated() {
		controller.update(PERSON_ID, new Person(0L, PERSON_GIVEN_NAME, PERSON_FAMILY_NAME));

		ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
		verify(controller.repository).update(captor.capture());

		Person person = captor.getValue();
		assertThat(person.getId(), is(PERSON_ID));
		assertThat(person.getGivenName(), is(PERSON_GIVEN_NAME));
		assertThat(person.getFamilyName(), is(PERSON_FAMILY_NAME));
	}
}
