package yasenovo.com.race;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public class ParticipantController {

    private final ParticipantRepository participantRepository;
    private final ParticipantService participantService;

    public ParticipantController(ParticipantRepository participantRepository, ParticipantService participantService) {
        this.participantRepository = participantRepository;
        this.participantService = participantService;
    }

    @GetMapping({"","/", "index.html"})
    public String mainPage() {
        return "index";
    }

    @GetMapping("/contact_us")
    public String contactsPage() {
        return "contact_us";
    }

    @GetMapping("/register")
    public String registerParticipant(Model model) {
        model.addAttribute("participant", new Participant());
        return "register";
    }

    @PostMapping("/saveOrUpdateParticipant")
    public String saveOrUpdateParticipant(@ModelAttribute Participant participant) {
        if(participant.getNumber() !=null && participant.getNumber() == 0){
            participant.setNumber(null);
        }
        if(participant.getSector() !=null && participant.getSector().isEmpty()){
            participant.setSector(null);
        }
        participantRepository.save(participant);
        return "redirect:/race";
    }

    @GetMapping("/race")
    public String displayRacePage(Model model) {
        model.addAttribute("participants", participantService.getAllParticipants());
        return "race";
    }

    @PostMapping("/shuffleParticipants")
    public String shuffleParticipants(Model model) {
        List<Participant> participants = participantRepository.findAll();
        Collections.shuffle(participants);
        for (int i = 0; i < participants.size(); i++) {
            participants.get(i).setNumber(i + 1);
            participantRepository.save(participants.get(i));
            model.addAttribute("participants", participants); // Update the model with shuffled list
        }
        return "redirect:/race";
    }

    @PostMapping("/clearNumbers")
    public String clearNumbers(Model model) {
        List<Participant> participants = participantRepository.findAll();
        Collections.shuffle(participants);
        for (int i = 0; i < participants.size(); i++) {
            participants.get(i).setNumber(null);
            participantRepository.save(participants.get(i));
            model.addAttribute("participa   nts", participants);
        }
        return "redirect:/race";
    }
    @PostMapping("/clearSectors")
    public String clearSectors(Model model) {
        List<Participant> participants = participantRepository.findAll();
        Collections.shuffle(participants);
        for (int i = 0; i < participants.size(); i++) {
            participants.get(i).setSector(null);
            participantRepository.save(participants.get(i));
            model.addAttribute("participants", participants);
        }
        return "redirect:/race";
    }

    @GetMapping("/participant/edit/{id}")
    public String editParticipant(@PathVariable int id, Model model) {
        Participant participant = participantService.getPersonById(id);
        model.addAttribute("participant", participant);
        return "edit";
    }

    @GetMapping("/participant/delete/{id}")
    public String deleteParticipant(@PathVariable int id) {
        participantService.deletePrincipalById(id);
        return "redirect:/race";
    }

    @GetMapping("/drawSector")
    public String drawSector(@RequestParam("participantId") Long participantId) {
        participantService.updateParticipantSector(participantId);
        return "redirect:/race";
    }
//======
@PersistenceContext
private EntityManager entityManager;
    @PostMapping("/truncateParticipants")
    @Transactional
    public String truncateParticipants() {
        entityManager.createNativeQuery("TRUNCATE TABLE participant").executeUpdate();
        return "redirect:/race";
    }

}
