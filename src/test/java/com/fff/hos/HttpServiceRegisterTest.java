package com.fff.hos;

import com.fff.hos.httpservice.person.HttpServiceRegister;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link HttpServiceRegister}.
 */
@RunWith(JUnit4.class)
public class HttpServiceRegisterTest {
    private static final String FAKE_URL = "fake.fk/register";
    // Set up a helper so that the ApiProxy returns a valid environment for local testing.
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper();

    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    private StringWriter responseWriter;
    private HttpServiceRegister servletUnderTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        helper.setUp();

        //  Set up some fake HTTP requests
        when(mockRequest.getRequestURI()).thenReturn(FAKE_URL);

        // Set up a fake HTTP response.
        responseWriter = new StringWriter();
        when(mockResponse.getWriter()).thenReturn(new PrintWriter(responseWriter));

        servletUnderTest = new HttpServiceRegister();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void doGetWritesResponse() throws Exception {
        servletUnderTest.doGet(mockRequest, mockResponse);

        // We expect our hello world response.
//    assertThat(responseWriter.toString())
//        .named("HelloAppEngine response")
//        .contains("Hello App Engine - Standard ");
    }

    @Test
    public void helloInfoTest() {
//    String result = HttpServiceRegister.getInfo();
//    assertThat(result)
//      .named("HelloInfo.getInfo")
//      .containsMatch("^Version:\\s+.+OS:\\s+.+User:\\s");
    }
}
