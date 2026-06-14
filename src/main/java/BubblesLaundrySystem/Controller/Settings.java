
package BubblesLaundrySystem.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class Settings {
    
    @GetMapping("/settings") public String showSettings(Model model) {
       
        model.addAttribute("detergentLimit", Inventory.detergentLimit);
        model.addAttribute("fabconLimit", Inventory.fabconLimit);
        model.addAttribute("bleachLimit", Inventory.bleachLimit);
        return "settings";
    }

   //when user wants to change their changes
    @GetMapping("/settings/save")
    public String saveSettings(
         @RequestParam(value = "detergent", defaultValue = "10") int detergent,
         @RequestParam(value = "fabcon", defaultValue = "10") int fabcon,
         @RequestParam(value = "bleach", defaultValue = "10") int bleach){
 
        Inventory.detergentLimit = detergent;
        Inventory.fabconLimit = fabcon;
        Inventory.bleachLimit = bleach;
        
        // Clean redirect back to settings view
        return "redirect:/settings";
    }
}