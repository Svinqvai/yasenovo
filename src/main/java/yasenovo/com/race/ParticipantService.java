package yasenovo.com.race;

import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Comparator.*;

@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    private List<String> sectors = List.of(
            "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9"
            , "Б1", "Б2", "Б3", "Б4", "Б5", "Б6", "Б7", "Б8", "Б9", "Б10"
            , "В1", "В2", "В3", "В4", "В5", "В6", "В7", "В8", "В9", "В10"
            , "Г1", "Г2", "Г3", "Г4", "Г5", "Г6", "Г7", "Г8", "Г9");
   private  List<String> availableSectors = new ArrayList<>(sectors);

    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public Participant getPersonById(long id) {
        Optional<Participant> personOptional = participantRepository.findById(id);
        if (personOptional.isPresent()) {
            return personOptional.get();
        } else {
            throw new RuntimeException("User with id " + id + " not found");
        }
    }

    public List<Participant> getAllParticipants() {
        List<Participant> participants = participantRepository.findAll();
        if (!participants.isEmpty()) {
            Participant participant = participants.get(0);
            if (participant.getNumber() != null) {
                participants.sort(nullsLast(comparing(Participant::getNumber, nullsLast(naturalOrder()))));
            } else {
                participants.sort(Comparator.comparingLong(Participant::getId));
            }
            return participants;
        } else {
            return Collections.emptyList();
        }
    }

    public String drawSector() {
        if (!availableSectors.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(availableSectors.size());
            return availableSectors.remove(randomIndex);
        }
        return "Няма свободен сектор";
    }

    public void updateParticipantSector(Long participantId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid participant Id: " + participantId));

        participant.setSector(drawSector());
        participantRepository.save(participant);
    }

    public void deletePrincipalById(long id) {
        participantRepository.deleteById(id);
    }
}
