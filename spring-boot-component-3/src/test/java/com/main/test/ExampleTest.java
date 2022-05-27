package com.main.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;

//@RunWith(MockitoJUnitRunner.class) // junit 4
@ExtendWith(MockitoExtension.class) // junit 5
public class ExampleTest {

    @Test
    void test()
    {
        //assertTrue("Hello".equals("hello"));
        assertEquals("Hello", "Hello");
    }
}
