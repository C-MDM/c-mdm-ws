package jp.co.cos_mos.mdm.ws.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import jp.co.cos_mos.mdm.core.service.domain.SequenceNumberServiceRequest;
import jp.co.cos_mos.mdm.core.service.domain.SequenceNumberServiceResponse;
import jp.co.cos_mos.mdm.core.service.domain.entity.Control;
import jp.co.cos_mos.mdm.core.service.domain.entity.SequenceNumberObj;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
//@ContextConfiguration(locations="classpath:rest-servlet.xml")
@ContextConfiguration(locations="file:src/test/resources/rest-servlet.xml")
public class SequenceNumberServiceControllerTest {

//	@InjectMocks
	@Autowired
    private SequenceNumberServiceController controller;
 
//	@Mock
//	private SequenceNumberService service;	
	
    private MockMvc mockMvc;
 
    private SequenceNumberObj testSequenceNumber;
    
    private ObjectMapper mapper;
    
    @Before
    public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller)
		    .setMessageConverters(new MappingJackson2HttpMessageConverter())
		    .build();
        
		mapper = new ObjectMapper();
			
		SequenceNumberServiceRequest request = 
				new SequenceNumberServiceRequest();
			
		Control control = new Control();
		SequenceNumberObj input = new SequenceNumberObj();
		input.setSeq("1");
		input.setName("ut");
		input.setInitialValue("0");
		input.setIncrementValue("1");
		input.setMaxValue("100");
		
		request.setControl(control);
		request.setInput(input);
		String content = "";
		try {
			content = mapper.writeValueAsString(request);

			ResultActions result = this.mockMvc.perform(
					post("/sequence/cerate")
					.content(content)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));

			SequenceNumberServiceResponse response =
					mapper.readValue(
							result.andReturn().getResponse().getContentAsString(), 
							SequenceNumberServiceResponse.class);
			
			testSequenceNumber = response.getOutput();
			assertTrue(input.getSeq().equals(testSequenceNumber.getSeq()));
			assertTrue(input.getName().equals(testSequenceNumber.getName()));
			assertTrue(input.getInitialValue().equals(testSequenceNumber.getInitialValue()));
			assertTrue(input.getIncrementValue().equals(testSequenceNumber.getIncrementValue()));
			assertTrue(input.getMaxValue().equals(testSequenceNumber.getMaxValue()));

		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    	
    }
    
    @Test
    public void testNumbering_SUCCESS001() throws Exception {
        ResultActions result = mockMvc.perform(
        		put("/sequence/numbering/" + testSequenceNumber.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		SequenceNumberServiceResponse response =
				mapper.readValue(
						result.andReturn().getResponse().getContentAsString(), 
						SequenceNumberServiceResponse.class);
		
		SequenceNumberObj output = response.getOutput();
        assertFalse(output.getSeq().equals(testSequenceNumber.getSeq()));
    }
    
    @Test
    public void testReset_SUCCESS001() throws Exception {
        ResultActions result = mockMvc.perform(
        		put("/sequence/reset/" + testSequenceNumber.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
        
		SequenceNumberServiceResponse response =
				mapper.readValue(
						result.andReturn().getResponse().getContentAsString(), 
						SequenceNumberServiceResponse.class);
		
		SequenceNumberObj output = response.getOutput();
        assertTrue(output.getSeq().equals(testSequenceNumber.getInitialValue()));
    }

}