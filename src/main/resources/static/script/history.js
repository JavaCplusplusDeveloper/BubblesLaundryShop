 function openDeductionsModal() {
                document.getElementById('deductionsModal').style.display = 'flex';
            }

            function closeDeductionsModal() {
                document.getElementById('deductionsModal').style.display = 'none';
            }

            // Close modal window cleanly if developer clicks on the translucent layout mask
            window.onclick = function(event) {
                var modal = document.getElementById('deductionsModal');
                if (event.target == modal) {
                    modal.style.display = 'none';
                }
            }