package com.rodrigotroy.learning.learningspringbatch;

import com.rodrigotroy.learning.learningspringbatch.domain.Person;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * Created with IntelliJ IDEA.
 * $ Project: learning-spring-batch
 * User: rodrigotroy
 * Date: 03-04-23
 * Time: 22:37
 */
public class RecordFieldSetMapper implements FieldSetMapper<Person> {

    @Override
    public Person mapFieldSet(FieldSet fieldSet) {
        return new Person(fieldSet.readString("firstName"),
                          fieldSet.readString("lastName"));
    }
}
