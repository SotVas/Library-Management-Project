function confirmDelete() {
    var deleteConfirmed = confirm("Are you sure you want to delete this book?");
    return deleteConfirmed;
}
// JavaScript function for filtering books based on the search input
function filterBooks() {
    var searchInput = document.getElementById('bookSearchInput').value.trim().toLowerCase();

    var books = document.querySelectorAll('.book-row');

    books.forEach(function (book) {
        var titleElement = book.querySelector('.title');
        var genreElement = book.querySelector('.genre');
        var authorElement = book.querySelector('.author');

        if (titleElement && genreElement && authorElement) {
            var title = titleElement.textContent.trim().toLowerCase();
            var genre = genreElement.textContent.trim().toLowerCase();
            var author = authorElement.textContent.trim().toLowerCase();

            // Check if the title or genre contains the search input
            var shouldDisplay = title.includes(searchInput) || genre.includes(searchInput) || author.includes(searchInput);

            // Toggle visibility based on the check
            book.style.display = shouldDisplay ? 'table-row' : 'none';
        }
    });
}

// Function to handle search on Enter key press
function handleBookSearchEnterKey(event) {
    if (event.key === 'Enter') {
        event.preventDefault(); // Prevent the default form submission behavior
        filterBooks();
    }
}

// Wait for the document to be fully loaded
document.addEventListener('DOMContentLoaded', function () {


    // Bind the filtering function to the keypress event on the search input
    document.getElementById('bookSearchInput').addEventListener('input', filterBooks);



});

$(function () {
    $('[data-toggle="tooltip"]').tooltip();
});

function confirmOrder() {
    if (confirm('Are you sure you want to Order this book?')) {
        alert('Order placed successfully!');
        return true;
    }
    return false;
}