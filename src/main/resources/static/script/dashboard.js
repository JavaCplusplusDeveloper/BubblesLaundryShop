function nextStep(stepNumber) {
    // If the user tries to click "Next" on Step 3 to reach Step 4
    if (stepNumber === 4) {
        let serviceMode = document.getElementById('serviceMode').value;
        let addrInput = document.getElementById('deliveryAddress');
        let descInput = document.getElementById('addressDescription');
        let addrError = document.getElementById('addressError');
        let descError = document.getElementById('descError');

        // Reset previous red borders and warning labels
        if (addrInput) addrInput.style.borderColor = "#cbd5e0";
        if (descInput) descInput.style.borderColor = "#cbd5e0";
        if (addrError) addrError.style.display = "none";
        if (descError) descError.style.display = "none";

        if (serviceMode === 'Delivery') {
            let isValid = true;

            // Validate Address field
            if (!addrInput || !addrInput.value.trim()) {
                if (addrInput) addrInput.style.borderColor = "#ff4d4d";
                if (addrError) addrError.style.display = "block";
                isValid = false;
            }
            
            // Validate Description field
            if (!descInput || !descInput.value.trim()) {
                if (descInput) descInput.style.borderColor = "#ff4d4d";
                if (descError) descError.style.display = "block";
                isValid = false;
            }

            // Halt execution immediately if a delivery field is empty
            if (!isValid) return;
        }
    }

    // Standard step transition block (now correctly within scope)
    document.querySelectorAll('.form-step').forEach(step => step.classList.remove('step-active'));
    let targetStep = document.getElementById('step-' + stepNumber);
    if (targetStep) targetStep.classList.add('step-active');
    
    if (stepNumber === 4) { calculateGrandTotal(); }
}

// Delivery Card Selection Toggles
function selectDeliveryMethod(method) {
    document.getElementById('serviceMode').value = method;
    document.getElementById('methodPickUp').classList.remove('active');
    document.getElementById('methodDelivery').classList.remove('active');
    
    let addrInput = document.getElementById('deliveryAddress');
    let descInput = document.getElementById('addressDescription');
    let addrError = document.getElementById('addressError');
    let descError = document.getElementById('descError');

    if (method === 'Pick-up') {
        document.getElementById('methodPickUp').classList.add('active');
        document.getElementById('deliveryAddressBlock').style.display = "none";
        
        // Wipe values and warnings clear so Pick-up transactions process effortlessly
        if (addrInput) { addrInput.value = ""; addrInput.style.borderColor = "#cbd5e0"; }
        if (descInput) { descInput.value = ""; descInput.style.borderColor = "#cbd5e0"; }
        if (addrError) addrError.style.display = "none";
        if (descError) descError.style.display = "none";
    } else {
        document.getElementById('methodDelivery').classList.add('active');
        document.getElementById('deliveryAddressBlock').style.display = "block";
    }
}

        function prevStep(stepNumber) {
            document.querySelectorAll('.form-step').forEach(step => step.classList.remove('step-active'));
            document.getElementById('step-' + stepNumber).classList.add('step-active');
        }

        // Counter Increment System
        function increment(inputId, max) {
            let input = document.getElementById(inputId);
            let val = parseInt(input.value);
            if(val < max || max === 99) { input.value = val + 1; }
            calculateGrandTotal();
        }

        // Counter Decrement System
        function decrement(inputId, min) {
            let input = document.getElementById(inputId);
            let val = parseInt(input.value);
            if(val > min) { input.value = val - 1; }
            calculateGrandTotal();
        }

     

        // Calculation Engine Engine
        function calculateGrandTotal() {
            let weightInput = document.getElementById('weight');
            let weight = parseInt(weightInput.value) || 1;
            let detergent = parseInt(document.getElementById('detergentQty').value) || 0;
            let fabcon = parseInt(document.getElementById('fabconQty').value) || 0;
            let bleach = parseInt(document.getElementById('bleachQty').value) || 0;
            let isRush = document.getElementById('isRush').checked;

            let baseLoadPrice = weight * 125.00;
            let totalAddons = (detergent * 12) + (fabcon * 10) + (bleach * 15);
            let finalGrandTotal = baseLoadPrice + totalAddons + (isRush ? 50.00 : 0.00);

            document.getElementById('basePricePreview').innerText = baseLoadPrice.toFixed(2);
            document.getElementById('sumLoadKg').innerText = weight;
            document.getElementById('sumDetergentCount').innerText = detergent;
            document.getElementById('sumFabconCount').innerText = fabcon;
            document.getElementById('sumBleachCount').innerText = bleach;
            document.getElementById('grandTotalDisplay').innerText = finalGrandTotal.toFixed(2);
            document.getElementById('totalAmountInput').value = finalGrandTotal.toFixed(2);
        }

        // Window Bind Controls for New Order Modal
        const modal = document.getElementById("orderModal");
        document.getElementById("openModalBtn").onclick = function() { 
            document.getElementById('weight').value = 1;
            document.getElementById('detergentQty').value = 0;
            document.getElementById('fabconQty').value = 0;
            document.getElementById('bleachQty').value = 0;
            document.getElementById('estimatedTime').value = "";
            document.getElementById('isRush').checked = false;
            calculateGrandTotal();
            modal.style.display = "flex"; 
            nextStep(1); 
        };
        document.getElementById("closeModalBtn").onclick = function() { modal.style.display = "none"; };
        document.getElementById("cancelModalBtn").onclick = function() { modal.style.display = "none"; };
        
        // --- IN-PAGE EDIT MODAL ENGINE ---
        const editModal = document.getElementById("editOrderModal");
        const editForm = document.getElementById("editForm");

        function triggerEditModal(element) {
            let id = element.getAttribute('data-id');
            let name = element.getAttribute('data-name');
            let weight = element.getAttribute('data-weight');
            let mode = element.getAttribute('data-mode');
            let time = element.getAttribute('data-time') || '3:00 pm';
            let detergent = element.getAttribute('data-detergent') || 0;
            let fabcon = element.getAttribute('data-fabcon') || 0;
            let bleach = element.getAttribute('data-bleach') || 0;

            editForm.action = "/transaction/update/" + id;
            
            document.getElementById("editCustomerName").value = name;
            document.getElementById("editWeight").value = Math.round(parseFloat(weight)) || 1;
            document.getElementById("editServiceMode").value = mode;
            document.getElementById("editEstimatedTime").value = time;
            document.getElementById("editAddonDetergent").value = parseInt(detergent);
            document.getElementById("editAddonFabcon").value = parseInt(fabcon);
            document.getElementById("editAddonBleach").value = parseInt(bleach);
            
            calculateEditTotal();
            editModal.style.display = "flex";
        }

        function incrementEditLoads() {
            let input = document.getElementById('editWeight');
            let val = parseInt(input.value) || 1;
            input.value = val + 1;
            calculateEditTotal();
        }

        function decrementEditLoads() {
            let input = document.getElementById('editWeight');
            let val = parseInt(input.value) || 1;
            if(val > 1) { input.value = val - 1; }
            calculateEditTotal();
        }

        function incrementEditAddon(inputId) {
            let input = document.getElementById(inputId);
            let val = parseInt(input.value) || 0;
            input.value = val + 1;
            calculateEditTotal();
        }

        function decrementEditAddon(inputId) {
            let input = document.getElementById(inputId);
            let val = parseInt(input.value) || 0;
            if(val > 0) { input.value = val - 1; }
            calculateEditTotal();
        }

        function calculateEditTotal() {
            let weight = parseInt(document.getElementById('editWeight').value) || 1;
            let detergent = parseInt(document.getElementById('editAddonDetergent').value) || 0;
            let fabcon = parseInt(document.getElementById('editAddonFabcon').value) || 0;
            let bleach = parseInt(document.getElementById('editAddonBleach').value) || 0;
            
            let baseLoadPrice = weight * 125.00;
            let totalAddons = (detergent * 12) + (fabcon * 10) + (bleach * 15);
            let total = baseLoadPrice + totalAddons;
            
            document.getElementById('editGrandTotalDisplay').innerText = total.toFixed(2);
            document.getElementById('editTotalAmountInput').value = total.toFixed(2);
        }

        document.getElementById("closeEditModalBtn").onclick = function() { editModal.style.display = "none"; };
        document.getElementById("cancelEditModalBtn").onclick = function() { editModal.style.display = "none"; };
        
        // Delete confirmation controllers
        const deleteModal = document.getElementById("deleteModal");
        const confirmDeleteLink = document.getElementById("confirmDeleteLink");
        
        function openDeleteModal(transactionId) {
            confirmDeleteLink.href = "/transaction/delete/" + transactionId;
            deleteModal.style.display = "flex";
        }
        
        document.getElementById("closeDeleteModalBtn").onclick = function() {
            deleteModal.style.display = "none";
        };

        // Form programmatic intercept submission handler
       // Strict Validation and Submit Engine for Step 4
document.getElementById("finishBtn").onclick = function() {
    let nameInput = document.getElementById('customerName');
    let phoneInput = document.getElementById('phoneNumber');
    let timeInput = document.getElementById('estimatedTime');
    
    let nameError = document.getElementById('nameError');
    let phoneError = document.getElementById('phoneError');
    let timeError = document.getElementById('timeError');
    
    // Clear out any old red alerts
    if (nameInput) nameInput.style.borderColor = "#cbd5e0";
    if (phoneInput) phoneInput.style.borderColor = "#cbd5e0";
    if (timeInput) timeInput.style.borderColor = "#cbd5e0";
    
    if (nameError) nameError.style.display = "none";
    if (phoneError) phoneError.style.display = "none";
    if (timeError) timeError.style.display = "none";
    
    let isStep4Valid = true;
    
    // Check Customer Name
    if (!nameInput || !nameInput.value.trim()) {
        if (nameInput) nameInput.style.borderColor = "#ff4d4d";
        if (nameError) nameError.style.display = "block";
        isStep4Valid = false;
    }
    
    // Check Phone Number
    if (!phoneInput || !phoneInput.value.trim()) {
        if (phoneInput) phoneInput.style.borderColor = "#ff4d4d";
        if (phoneError) phoneError.style.display = "block";
        isStep4Valid = false;
    }
    
    // Check Target Time
    if (!timeInput || !timeInput.value.trim()) {
        if (timeInput) timeInput.style.borderColor = "#ff4d4d";
        if (timeError) timeError.style.display = "block";
        isStep4Valid = false;
    }
    
    // Only allow submission if all conditions pass completely
    if (isStep4Valid) {
        document.getElementById('multiStepForm').submit();
    }
};