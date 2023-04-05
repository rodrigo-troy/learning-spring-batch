package com.rodrigotroy.learning.learningspringbatch;

import com.rodrigotroy.learning.learningspringbatch.domain.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * Created with IntelliJ IDEA.
 * $ Project: learning-spring-batch
 * User: rodrigotroy
 * Date: 03-04-23
 * Time: 21:45
 * <p>
 * PersonItemProcessor implements Spring Batch’s ItemProcessor interface. This makes it easy to wire the code into a batch job. According to the interface, you receive an incoming Person object, after which you transform it to an upper-cased Person.
 * <p>
 */
public class PersonItemProcessor implements ItemProcessor<Person, Person> {
    private static final Logger LOG = LoggerFactory.getLogger(PersonItemProcessor.class);

    /**
     * This method processes a Person object by capitalizing the first and last names and returning a new Person object.
     *
     * @param person the input Person object
     * @return the transformed Person object with capitalized first and last names
     */
    @Override
    public Person process(final Person person) {
        LOG.info("Processing person: %s".formatted(person));

        final String firstName = person.firstName().toUpperCase();
        final String lastName = person.lastName().toUpperCase();

        final Person transformedPerson = new Person(firstName,
                                                    lastName);

        LOG.info("Converting (%s) into (%s)".formatted(person,
                                                       transformedPerson));

        return transformedPerson;
    }

}
