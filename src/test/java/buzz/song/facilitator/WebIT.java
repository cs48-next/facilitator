package buzz.song.facilitator;

import buzz.song.facilitator.model.Venue;
import buzz.song.facilitator.model.request.VenueCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class WebIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void controllerTests() throws Exception {
		final VenueCreateRequest createRequest = new VenueCreateRequest("venue", 200, 300);
		final String createPayload = objectMapper.writeValueAsString(createRequest);

		final MvcResult result = mockMvc.perform(post("/venue")
				.contentType(MediaType.APPLICATION_JSON)
				.content(createPayload)
		).andReturn();

		final ResultActions actions = mockMvc.perform(asyncDispatch(result))
				.andExpect(status().isOk());

		final Venue venue = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Venue.class);

		final String venueId = venue.getId();

		Assert.assertEquals("venue", venue.getName());
		Assert.assertEquals(200, venue.getLatitude(), 0);
		Assert.assertEquals(300, venue.getLongitude(), 0);
	}
}
