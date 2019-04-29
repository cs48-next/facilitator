package buzz.song.facilitator;

import buzz.song.facilitator.model.Track;
import buzz.song.facilitator.model.Venue;
import buzz.song.facilitator.model.Vote;
import buzz.song.facilitator.model.request.VenueCreateRequest;
import buzz.song.facilitator.model.response.VenueListResponse;
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

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

		final MvcResult createResult = mockMvc.perform(post("/venue")
				.contentType(MediaType.APPLICATION_JSON)
				.content(createPayload)
		).andReturn();
		final ResultActions createActions = mockMvc.perform(asyncDispatch(createResult)).andExpect(status().isOk());

		final Venue venue = objectMapper.readValue(createActions.andReturn().getResponse().getContentAsString(), Venue.class);

		Assert.assertEquals("venue", venue.getName());
		Assert.assertEquals(200, venue.getLatitude(), 0);
		Assert.assertEquals(300, venue.getLongitude(), 0);

		final MvcResult proposeResult = mockMvc.perform(put("/track/" + venue.getId() + "/Tra.12")).andReturn();
		final ResultActions proposeActions = mockMvc.perform(asyncDispatch(proposeResult)).andExpect(status().isOk());

		final Track track = objectMapper.readValue(proposeActions.andReturn().getResponse().getContentAsString(), Track.class);

		Assert.assertEquals("Tra.12", track.getTrackId());
		Assert.assertEquals(venue.getId(), track.getVenueId());
		Assert.assertEquals(0, track.getVotes().size());

		final MvcResult voteResult = mockMvc.perform(put("/vote/" + venue.getId() + "/Tra.12/upvote")).andReturn();
		final ResultActions voteActions = mockMvc.perform(asyncDispatch(voteResult)).andExpect(status().isOk());

		final Vote vote = objectMapper.readValue(voteActions.andReturn().getResponse().getContentAsString(), Vote.class);

		Assert.assertEquals("Tra.12", vote.getTrackId());
		Assert.assertEquals(venue.getId(), vote.getVenueId());
		Assert.assertTrue(vote.isUpvote());

		final MvcResult listVenues = mockMvc.perform(get("/venue?latitude=200&longitude=300")).andReturn();
		final ResultActions listActions = mockMvc.perform(asyncDispatch(listVenues)).andExpect(status().isOk());

		final VenueListResponse listings = objectMapper.readValue(listActions.andReturn().getResponse().getContentAsString(), VenueListResponse.class);
		final Optional<VenueListResponse.VenueListing> listing = listings.getVenues().stream().filter(v -> v.getVenueId().equals(venue.getId())).findFirst();

		Assert.assertTrue(listing.isPresent());
		Assert.assertEquals(0, listing.get().getDistance(), 0);
		Assert.assertEquals("venue", listing.get().getVenueName());
		Assert.assertEquals(venue.getId(), listing.get().getVenueId());

		final MvcResult fetchVenue = mockMvc.perform(get("/venue/" + venue.getId())).andReturn();
		final ResultActions fetchActions = mockMvc.perform(asyncDispatch(fetchVenue)).andExpect(status().isOk());

		final Venue fetchedVenue = objectMapper.readValue(fetchActions.andReturn().getResponse().getContentAsString(), Venue.class);

		Assert.assertEquals(1, fetchedVenue.getPlaylist().size());
		Assert.assertEquals(1, fetchedVenue.getPlaylist().first().getVotes().stream().mapToInt(v -> v.isUpvote() ? 1 : -1).sum());

	}
}
