package com.mitrais.cdc.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

class ReferenceNumberGeneratorTest {
    private Random random;

    @BeforeEach
    void setUp() {
        random = Mockito.mock(Random.class);
    }

    @Test
    void testGenerateTransferRefnumber_withRandomNumberLessThan7Character_thenReturnReferenceNumberWithPaddedZeroUntil7Character() {
        Mockito.when(random.nextInt(Mockito.anyInt())).thenReturn(255);
        String result = ReferenceNumberGenerator.generateTransferRefnumber(random);
        Assertions.assertEquals("T255000", result);
    }

    @Test
    void generateWithdrawRefnumber_withRandomNumberLessThan7Character_thenReturnReferenceNumberWithPaddedZeroUntil7Character() {
        Mockito.when(random.nextInt(Mockito.anyInt())).thenReturn(255);
        String result = ReferenceNumberGenerator.generateWithdrawRefnumber(random);
        Assertions.assertEquals("W255000", result);
    }
}