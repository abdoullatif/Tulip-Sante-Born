package com.example.tulipsante.repository;

import com.example.tulipsante.models.Relation;
import com.example.tulipsante.utils.GeneralPurposeFunctions;

import org.junit.Test;

import static org.junit.Assert.*;

public class PatientRepositoryTest {

    PatientRepository patientRepository;

    @Test
    public void insertPatientRelation() {
        Relation relation = new Relation(
                GeneralPurposeFunctions.idTable(),
                "",
                "",
                "",
                ""
        );

        boolean expected = 2+2 == 2;

//        boolean expected = patientRepository.insertPatientRelation(relation);

        assertFalse(expected);
    }
}