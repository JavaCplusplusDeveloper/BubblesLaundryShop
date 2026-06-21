function saveAlertThresholds() {
        let detergent = document.getElementById('detergentInput').value;
        let fabcon = document.getElementById('fabconInput').value;
        let bleach = document.getElementById('bleachInput').value;
        
        // Fires all updated values over to your Spring Boot Controller route 
        window.location.href = "/settings/save?detergent=" + detergent + "&fabcon=" + fabcon + "&bleach=" + bleach;
    }