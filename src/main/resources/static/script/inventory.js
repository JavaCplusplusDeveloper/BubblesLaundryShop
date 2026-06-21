  // Tracks active target row
    let activeItemId = null;

    // Accepts the button element ('this') instead of raw strings
    function openRestockModal(button){
        // Pull the safe embedded data attributes from the button
        activeItemId = button.getAttribute('data-id');
        let itemName = button.getAttribute('data-name');
        
        document.getElementById('restockItemTarget').innerText = "Add stock volume for: " + itemName;
        document.getElementById('restockAmountInput').value = ""; // Clear old residual values
        document.getElementById('restockModal').style.display = "flex";
    }

    // Closes context UI wrapper box safely
    document.getElementById('closeRestockModalBtn').addEventListener('click', function() {
        document.getElementById('restockModal').style.display = "none";
    });

    // Handles secure redirection processing straight into Spring backend controller endpoints
    function submitRestockData() {
        let amount = document.getElementById('restockAmountInput').value;
        if(amount && !isNaN(amount) && amount > 0){
            window.location.href = "/inventory/restock/" + activeItemId + "?amount=" + amount;
        }
    }
    