package com.simple.server.dao;

import com.simple.server.model.Person;
import org.hibernate.dialect.H2Dialect;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

import javax.sql.DataSource;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PersonRepositoryIntegrationTest {

	private static final long PERSON_ID = 1L;
	private static final String ALBERT = "Albert";
	private static final String EINSTEIN = "Einstein";
	private static final String NEILS = "Neils";
	private static final String BOHR = "Bohr";

	private final PersonRepository repository = new PersonRepository();

	@Before
	public void init() throws Exception {
		DataSource dataSource = new DriverManagerDataSource("jdbc:h2:mem:hibernate;DB_CLOSE_DELAY=-1", "admin", "password");

		System.setProperty("hibernate.dialect", H2Dialect.class.getName());
		System.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		AnnotationSessionFactoryBean sessionFactory = new AnnotationSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setAnnotatedClasses(new Class[] { Person.class});
		sessionFactory.afterPropertiesSet();

		repository.hibernate = new HibernateTemplate(sessionFactory.getObject());
	}

	@Test
	public void personIsPersistedAndReturnedFromDB() {
		repository.hibernate.clear();
		repository.hibernate.save(new Person(PERSON_ID, ALBERT, EINSTEIN));

		List<Person> persons = repository.getAll();
		assertThat(persons.size(), is(1));

		Person person = persons.get(0);
		assertThat(person.getId(), is(PERSON_ID));
		assertThat(person.getGivenName(), is(ALBERT));
		assertThat(person.getFamilyName(), is(EINSTEIN));
	}

	@Test
	public void personIsDeletedFromDB() {
		repository.hibernate.clear();
		repository.hibernate.save(new Person(PERSON_ID, ALBERT, EINSTEIN));

		repository.delete(PERSON_ID);

		List<Person> persons = repository.getAll();
		assertThat(persons.size(), is(0));
	}

	@Test
	public void personIsUpdatedInDB() {
		repository.hibernate.clear();
		repository.hibernate.save(new Person(PERSON_ID, ALBERT, EINSTEIN));

		repository.update(new Person(PERSON_ID, NEILS, BOHR));

		List<Person> persons = repository.getAll();
		assertThat(persons.size(), is(1));

		Person person = persons.get(0);
		assertThat(person.getId(), is(PERSON_ID));
		assertThat(person.getGivenName(), is(NEILS));
		assertThat(person.getFamilyName(), is(BOHR));
	}
}
