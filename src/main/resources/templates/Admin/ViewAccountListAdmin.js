const search = document.querySelector('.input-group input'),
    table_rows = document.querySelectorAll('tbody tr'),
    table_headings = document.querySelectorAll('thead th');

let currentSortColumn = 0;
let currentSortAsc = true;

// 1. Searching for specific data of HTML table
search.addEventListener('input', () => {
    searchTable();
    sortTable(currentSortColumn, currentSortAsc);
});

function searchTable() {
    const tbody = document.querySelector('tbody');
    const search_data = search.value.toLowerCase();
    const matchingRows = [];
    const nonMatchingRows = [];

    table_rows.forEach(row => {
        let table_data = row.textContent.toLowerCase();
        if (table_data.indexOf(search_data) < 0) {
            row.classList.add('hide');
            nonMatchingRows.push(row);
        } else {
            row.classList.remove('hide');
            matchingRows.push(row);
        }
    });

    tbody.innerHTML = ''; // Clear the table body
    matchingRows.forEach(row => tbody.appendChild(row));
    nonMatchingRows.forEach(row => tbody.appendChild(row));

    updateRowColors();
}

function updateRowColors() {
    document.querySelectorAll('tbody tr:not(.hide)').forEach((visible_row, i) => {
        visible_row.style.backgroundColor = (i % 2 === 0) ? 'transparent' : '#0000000b';
    });
}



// 2. View Button
document.querySelectorAll('.view-button').forEach(button => {
    button.addEventListener('click', function() {
        const row = button.closest('tr');
        const accountName = row.children[1].textContent; // Assuming the Name is in the second column
        alert('Viewing account: ' + accountName);
    });
});
