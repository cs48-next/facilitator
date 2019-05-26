package buzz.song.facilitator;

import buzz.song.facilitator.model.Venue;
import buzz.song.facilitator.repository.*;
import buzz.song.facilitator.service.VenueService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTests {

	private static final double LONGITUDE_LEN_EQUATOR_MI = 69.1710411;
	private static final double LATITUDE_LEN_EQUATOR_MI =  68.70749821;

	@Mock private VenueRepository venueRepo;
	@Mock private VenueStatsRepository statsRepo;
	@Mock private TrackRepository trackRepo;
	@Mock private VoteRepository voteRepo;
	@Mock private VoteSkipRepository voteSkipRepo;

	@InjectMocks
	private VenueService venueService;

	@Test
	public void testVenueService() {
		final Map<String, Venue> venues = new HashMap<>();
		when(venueRepo.save(any())).then(invocationOnMock -> {
			final Venue created = invocationOnMock.getArgument(0);
			venues.put(created.getId(), created);
			return created;
		});
		when(venueRepo.findById(anyString())).then(invocationOnMock -> Optional.ofNullable(venues.get(invocationOnMock.getArgument(0))));
		when(venueRepo.findAll()).then(invocationOnMock -> new ArrayList<>(venues.values()));

		final Venue venue = venueService.createVenue("venue", "host", "host_id", 0, 0).join();
		Assert.assertEquals("venue", venue.getName());
		Assert.assertEquals("host", venue.getHostName());
		Assert.assertEquals("host_id", venue.getHostId());

		final Venue fetchedVenue = venueService.fetchVenue(venue.getId()).join();
		Assert.assertEquals("venue", fetchedVenue.getName());
		Assert.assertEquals("host", fetchedVenue.getHostName());
		Assert.assertEquals("host_id", fetchedVenue.getHostId());

		final Map<Venue, Double> venueList = venueService.listVenues(1, 1).join();
		Assert.assertTrue(venueList.containsKey(fetchedVenue));
		Assert.assertEquals(Math.sqrt(Math.pow(LONGITUDE_LEN_EQUATOR_MI, 2) + Math.pow(LATITUDE_LEN_EQUATOR_MI, 2)), venueList.get(fetchedVenue), 0.5);

	}
}
