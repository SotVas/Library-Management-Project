function toggleEdit() {
    var editFieldsContainer = document.getElementById('editFields');
    var editButton = document.querySelector('.btn-dark-green');
    var spans = document.querySelectorAll('[id$=Span]');
    var inputs = document.querySelectorAll('[id$=Input]');

    if (editFieldsContainer.style.display === 'none') {
        // Switching to edit mode
        editFieldsContainer.style.display = 'block';
        editButton.style.display = 'none';  // Hide the Edit button

        // Toggle visibility of spans and inputs
        spans.forEach(function (span) {
            span.style.display = 'none';
        });

        inputs.forEach(function (input) {
            input.style.display = 'block';
        });
    } else {
        // Switching back to view mode
        editFieldsContainer.style.display = 'none';
        editButton.style.display = 'inline';  // Show the Edit button

        // Toggle visibility of spans and inputs
        spans.forEach(function (span) {
            span.style.display = 'inline';
        });

        inputs.forEach(function (input) {
            input.style.display = 'none';
        });
    }
}


function cancelEdit() {
    var editFieldsContainer = document.getElementById('editFields');
    var editButton = document.querySelector('.btn-dark-green');
    var spans = document.querySelectorAll('[id$=Span]');
    var inputs = document.querySelectorAll('[id$=Input]');

    // Switching back to view mode
    editFieldsContainer.style.display = 'none';
    editButton.style.display = 'inline';  // Show the Edit button

    // Toggle visibility of spans and inputs
    spans.forEach(function (span) {
        span.style.display = 'inline';
    });

    inputs.forEach(function (input) {
        input.style.display = 'none';
    });
}


function saveChanges() {
    // Handle the save changes logic here (submit the form if needed)
    document.querySelector('form').submit();
}