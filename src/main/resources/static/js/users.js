$(document).ready(function () {
    $('#userSearchInput').on('input', function () {
        var searchInput = $(this).val().toLowerCase().trim();

        $('.user-row').each(function () {
            var idElement = $(this).find('.user-id');
            var firstNameElement = $(this).find('.user-firstname');
            var lastNameElement = $(this).find('.user-lastname');
            var usernameElement = $(this).find('.user-username');
            var userRoleElement = $(this).find('.user-roleByUserrole');

            var id = idElement.text().toLowerCase().trim();
            var firstName = firstNameElement.text().toLowerCase().trim();
            var lastName = lastNameElement.text().toLowerCase().trim();
            var username = usernameElement.text().toLowerCase().trim();
            var userRole = userRoleElement.text().toLowerCase().trim();

            var shouldDisplay = id.includes(searchInput) || lastName.includes(searchInput) || username.includes(searchInput) || firstName.includes(searchInput) || userRole.includes(searchInput);

            // Toggle visibility
            $(this).toggle(shouldDisplay);

            // Highlight matching text
            if (shouldDisplay) {
                highlightText(idElement, searchInput);
                highlightText(firstNameElement, searchInput);
                highlightText(lastNameElement, searchInput);
                highlightText(usernameElement, searchInput);
                highlightText(userRoleElement, searchInput);
            }
        });
    });
});

function highlightText(element, searchText) {
    var text = element.text();
    var highlightedText = text.replace(new RegExp('(' + searchText + ')', 'gi'), '<span class="highlight">$1</span>');
    element.html(highlightedText);
}
