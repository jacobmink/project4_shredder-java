package shredder;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

@RestController
public class TrailController {

    @Autowired
    public TrailRepository trailRepository;

    @GetMapping("/trails")
    public Iterable<Trail> getTrails(){
        Iterable<Trail> trailList = trailRepository.findAll();
        return trailList;
    }
}
