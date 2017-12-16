/*
DESCRIPTION:
This is a java class unit test suite for the simplest reservation room finding strategy possible; first available.
It's a classic example of how something very trivial can be full of edge-cases. Note how every test signature and body is self-explaining. Also, no test is longer than 3 lines of code and follow a gherkin pattern according to the name of the test.

Finally, this code is 100% mutation tested, meaning that any mutation made to the tested code would actually make one or more test fail.

*/

package ...;

import ...;

@RunWith(MockitoJUnitRunner.class)
public class FirstAvailableRoomFinderStrategyTest {

    private static final int A_CAPACITY = 1;
    private static final Priority A_PRIORITY = Priority.ONE;
    private static final String RESERVATION_TITLE = "reservation title";
    private static final String RESERVATION_ORGANIZER = "reservation organizer";
    private static final String RESERVATION_ACCOUNTANT = "reservation accountant";
    private static final List<String> PARTICIPANT_EMAIL_LIST = new ArrayList<String>();
    private static final ParticipantsInformations participantsInformations = new ParticipantsInformations(RESERVATION_ORGANIZER, RESERVATION_ACCOUNTANT,
            PARTICIPANT_EMAIL_LIST);
    private Reservation reservation;
    @Mock
    private Room firstAvailableRoom;
    @Mock
    private Room secondAvailableRoom;
    @Mock
    private Room firstNotAvailableRoom;
    private List<Room> rooms;
    private FirstAvailableRoomFinderStrategy assignationStrategy;

    @Before
    public void setUp() {
        reservation = new Reservation(A_CAPACITY, A_PRIORITY, participantsInformations, RESERVATION_TITLE);
        assignationStrategy = new FirstAvailableRoomFinderStrategy();
        rooms = new ArrayList<Room>();
    }

    @Test(expected = NoRoomAvailableException.class)
    public void givenAnEmptyListOfRoom_WhenListIsEmpty_ThenThrowNoRoomAvailableException() {
        assignationStrategy.findRoom(reservation, rooms);
    }

    @Test(expected = NoRoomAvailableException.class)
    public void givenARoomThatCantAccomodateRequest_WhenFindingARoom_ThenShouldThrowNoRoomAvailableException() {
        prepareOneRoomThatDoesNotAccomodate();
        assignationStrategy.findRoom(reservation, rooms);
    }

    @Test
    public void givenTwoRoomsWithEnoughSpace_WhenFindingRoom_ThenReturnTheFirstOne() {
        prepareAListOfTwoAvailableRoomWithEnoughSpace();
        Room roomReturned = assignationStrategy.findRoom(reservation, rooms);
        assertEquals(firstAvailableRoom, roomReturned);
    }

    @Test
    public void givenTwoRoomsWithEnoughSpace_WhenOneIsNotAvailableButTheOtherIs_ThenReturnTheOneAvailable() {
        prepareOneNotAvailableRoomAndOneAvailable();
        Room roomReturned = assignationStrategy.findRoom(reservation, rooms);
        assertEquals(firstAvailableRoom, roomReturned);
    }

    @Test
    public void givenTwoAvailableRooms_WhenOneIsTooSmallAndTheOtherIsNot_ThenReturnTheOneThatCanAccomodateSpace() {
        prepareTwoAvailableRoomWithOneThatAccomodateAndOneThatDoesNot();
        Room roomReturned = assignationStrategy.findRoom(reservation, rooms);
        assertEquals(secondAvailableRoom, roomReturned);
    }

    private void prepareOneRoomThatDoesNotAccomodate() {
        when(firstAvailableRoom.canAccomodate(any(Reservation.class))).thenReturn(false);
        rooms.add(firstAvailableRoom);
    }

    private void prepareTwoAvailableRoomWithOneThatAccomodateAndOneThatDoesNot() {
        when(firstAvailableRoom.isAvailable()).thenReturn(true);
        when(firstAvailableRoom.canAccomodate(any(Reservation.class))).thenReturn(false);
        rooms.add(firstAvailableRoom);
        when(secondAvailableRoom.isAvailable()).thenReturn(true);
        when(secondAvailableRoom.canAccomodate(any(Reservation.class))).thenReturn(true);
        rooms.add(secondAvailableRoom);
    }

    private void prepareAListOfTwoAvailableRoomWithEnoughSpace() {
        when(firstAvailableRoom.isAvailable()).thenReturn(true);
        when(firstAvailableRoom.canAccomodate(any(Reservation.class))).thenReturn(true);
        rooms.add(firstAvailableRoom);
        when(secondAvailableRoom.isAvailable()).thenReturn(true);
        when(secondAvailableRoom.canAccomodate(any(Reservation.class))).thenReturn(true);
        rooms.add(secondAvailableRoom);
    }

    private void prepareOneNotAvailableRoomAndOneAvailable() {
        when(firstAvailableRoom.isAvailable()).thenReturn(true);
        when(firstAvailableRoom.canAccomodate(any(Reservation.class))).thenReturn(true);
        when(secondAvailableRoom.isAvailable()).thenReturn(true);
        when(secondAvailableRoom.canAccomodate(any(Reservation.class))).thenReturn(true);
        rooms.add(firstNotAvailableRoom);
        rooms.add(firstAvailableRoom);
    }
}