/**
 * Declaration of global variables
 */
let myHeaders = new Headers()
let sharedStockApiUrl = "/manager/api/sharedStock"
let stockApiUrl = "/manager/api/stock"
myHeaders.append('Content-type', 'application/json; charset=UTF-8')
const lastPage = {active: true, number: 0, last: false};

$(document).ready(function () {

    fetchStockList("/page")

    /**
     * buttons 'click' event listeners
     */
    /*Sort Buttons*/
    document.getElementById('sortUp').addEventListener('click', handleSortButton)
    document.getElementById('sortDown').addEventListener('click', handleSortButton)
    /*Filter Buttons*/
    document.getElementById('stockFilters').addEventListener('click', defineFilterAndFetchList)
    /*New stock*/
    document.getElementById('newStockButton').addEventListener('click', handleAddNewStockButton)
    /*Modal window buttons*/
    document.getElementById('modalFooter').addEventListener('click', checkFields)
    document.getElementById('stocksDiv').addEventListener('click', handleStockDivButtons)

    handleSummernote()

    $(window).scroll(yHandler);
});

function yHandler() {
    if (lastPage.last || !lastPage.active) {
        return;
    }
    let stocksDiv = document.getElementById('stocksDiv');
    let contentHeight = stocksDiv.offsetHeight;
    let yOffset = window.pageYOffset;
    let y = yOffset + window.innerHeight;
    if (y >= contentHeight) {
        fetchStockList('/page', false);
    }
}

/**
 * function validate fields in modal window
 * @param event
 */
function checkFields(event) {
    let stockTitle = document.getElementById('stockTitle')
    let stockText = document.getElementById('stockText')
    let startDate = document.getElementById('startDate')
    if (stockTitle.value === '') {
        invalidModalField("Заполните заголовок акции", stockTitle)
    } else if (stockText.value === "") {
        invalidModalField("Заполните описание акции", stockText)
    } else if (startDate.value === '') {
        invalidModalField("Заполните начальную дату", startDate)
    } else {
        handleSaveChangesButton(event)
    }
}

/**
 * function handles edit and delete buttons of main table
 * @param event
 */
function handleStockDivButtons(event) {
    let button = event.target.dataset.toggleId;
    if (button === "edit-stock") {
        handleEditButtonClick(event)
    } else if (button === "delete-stock") {
        handleDeleteButtonClick(event)
    }
}

/**
 * function defines filter for fetch
 * @param event
 */
function defineFilterAndFetchList(event) {
    let filter = `/${event.target.dataset.toggleId}`
    if (filter === '/page') {
        lastPage.active = true;
        lastPage.number = 0;
        lastPage.last = false;
    }else {
        lastPage.active = false;
    }
    fetchStockList(filter)
}

/**
 * Fetch request to stock list
 * @param filter - one of the following filters:
 *  - /allStocks
 *  - /currentStocks
 *  - /futureStocks"
 *  - /pastStocks"
 */
function fetchStockList(filter) {
    $.ajax(stockApiUrl + filter, {
        headers: myHeaders,
        data: {page: lastPage.number},
        async: !lastPage.active,
        success: function (data) {
            console.log("typeof data: " + typeof data);
            console.log(data);
            if (Array.isArray(data)) {
                renderStockList(data);
            } else {
                lastPage.number = data.number + 1;
                lastPage.last = data.last;
                renderStockList(data.content);
            }
        },
        error: printStocksNotFoundMessage
    });
}

/**
 * function writes message that stocks not found
 */
function printStocksNotFoundMessage() {
    $("#stocksDiv").empty().append(`<Strong>Акций не найдено</Strong>`)
}

/**
 * function handles sort buttons 'click'
 */
function handleSortButton() {
    let list = document.getElementById('stocksDiv')
    let items = document.querySelectorAll('#stocksDiv li')
    let checkButton = $(this).attr('data-sort')
    let sorted = [...items].sort(function (a, b) {
        if (checkButton === "sortDown") {
            return b.getAttribute('rating') - a.getAttribute('rating');
        }
        return a.getAttribute('rating') - b.getAttribute('rating');
    })
    list.innerHTML = ''
    for (let li of sorted) {
        list.appendChild(li)
    }
}

/**
 * delete button handler
 * @param event - эвент откуда берем id элемента
 */
function handleDeleteButtonClick(event) {
    let stockId = event.target.dataset.stockId
    let doDelete = confirm(`Удалить акцию id: ${stockId}?`);
    if (doDelete) {
        console.log(`${stockId} will be deleted`)
        fetch(stockApiUrl + `/${stockId}`, {
            method: 'DELETE',
            headers: myHeaders
        }).then(function (response) {
            if (response.status === 200) {
                successActionMainPage("#mainWindowAlert", "Акция успешно удалена", "success")
                $(`#li-${stockId}`).remove()
            } else {
                successActionMainPage("#mainWindowAlert", "Акция не найдена", "error")
            }
        })
    }
    $('#stockModal').modal('hide')
}

/**
 * modal window "save changes" button handler
 */
function handleSaveChangesButton() {
    let startDate = $('#startDate').val()
    startDate = moment(startDate).format("YYYY-MM-DD")
    let endDate = ""
    if ($('#endDate').val() !== null || $('#endDate').val() !== "") {
        endDate = $('#endDate').val()
        endDate = moment(endDate).format("YYYY-MM-DD")
    }
    if (endDate === "Invalid date") {
        endDate = ""
    }

    const stock = {
        id: $('#stockId').val(),
        stockImg: $('#stockImg').val(),
        stockTitle: $('#stockTitle').val(),
        stockText: $('#stockText').summernote('code'),
        startDate: startDate,
        endDate: endDate,
        stock: $('#stockTimeZone').val(),
    }
    let method = (stock.id !== '' ? 'PUT' : 'POST')

    fetchStock(stock, method)

    function fetchStock(stock, method) {
        fetch(stockApiUrl, {
            method: method,
            headers: myHeaders,
            body: JSON.stringify(stock)
        }).then(function (response) {
            if (response.status === 200) {
                successActionMainPage("#mainWindowAlert", "Акция успешно сохранена", "success")
            } else {
                successActionMainPage("#mainWindowAlert", "Акция не сохранена", "error")
            }
        })
    }

    $('#stockModal').modal('hide')
}

/**
 * Edit button handler
 * @param event
 */
function handleEditButtonClick(event) {
    $('#stockIdDiv').removeClass('d-none')
    $('.modal-title').text("Редактировать акцию")
    let stockId = event.target.dataset.stockId
    stockModalClearFields()

    function renderModalWindowEdit(stock) {
        let stockText = stock.stockText
        $("#stockId").val(stock.id)
        $("#stockTitle").val(stock.stockTitle)
        $('#stockText').summernote('code', stockText)
        $("#startDate").val(stock.startDate)
        $("#endDate").val(stock.endDate)
    }

    fetch(stockApiUrl + `/${stockId}`, {
        method: 'GET',
        headers: myHeaders
    }).then(response => response.json()).then(stock => renderModalWindowEdit(stock))
}

/**
 * function changes modal window header
 */
function handleAddNewStockButton() {
    $('#stockIdDiv').addClass('d-none')
    $('.modal-title').text("Добавить новую акцию")
    stockModalClearFields()
}

/**
 * function clears modal window fields
 */
function stockModalClearFields() {
    $("#stockId").val("")
    $("#stockTitle").val("")
    $('#stockText').summernote('code', "")
    $("#startDate").val("")
    $("#endDate").val("")
}

/**
 * Stock list render
 * @param stocks stocks from db
 */
function renderStockList(stocks) {
    let stockDiv = $("#stocksDiv");
    if (!lastPage.active || lastPage.number === 0) {
        $(stockDiv).empty();
    }
    $.ajax(sharedStockApiUrl, {
        headers: myHeaders,
        async: !lastPage.active,
        success: render
    });

    function render(sharedStocks) {
        console.log("stocks:");
        console.log(stocks);
        let sharedStocksQuantity = sharedStocks.length
        for (let i = 0; i < stocks.length; i++) {
            let rating = Math.round(stocks[i].sharedStocks.length / sharedStocksQuantity * 10)
            let endDate = stocks[i].endDate
            if (endDate === null) {
                endDate = "бессрочно"
            } else {
                endDate = moment(endDate).format("DD MMM YYYY")
            }
            let row = $(`<li id=li-${stocks[i].id}>`).attr("rating", rating);
            row.append(`<div class=\"card mb-3\">
                        <div class=\"row no-gutters\">
                            <div class=\"col-md-4\">
                                 <img class=\"card-img\" src=\"../static/img/stocks/1.jpg\" width=\"250\">
                            </div>
                            <div class=\"col-md-6\">
                                <div class=\"card-body\">
                                    <h3 class='card-title'>${stocks[i].stockTitle}</h3>
                                    <p class=\"card-text\">${stocks[i].stockText}</p>
                                    <p id="rating" class="rating">Рейтинг: ${rating}</p>
                                    <p>Срок проведения акции: </p>
                                    <div class=\"card-date\">
                                         с ${moment(stocks[i].startDate).format("DD MMM")}
                                         по ${endDate}
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-2 flex-row align-items-center">
                                <div class="nav flex-column nav-pills mt-2 container-fluid" role="tablist" aria-orientation="vertical">
                                    <button data-toggle-id="edit-stock" class="btn btn-info" data-toggle='modal'
                                            data-target='#stockModal' id="editButton" data-stock-id="${stocks[i].id}">Edit</button>
                                </div>
                                <div  class="nav flex-column nav-pills mt-2 container-fluid" role="tablist" aria-orientation="vertical">
                                    <button data-toggle-id="delete-stock" class="btn btn-danger" id="deleteButton" data-stock-id="${stocks[i].id}">Delete</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </li> `)
            stockDiv.append(row)
        }
    }
}

function handleSummernote() {
    $('#stockText').summernote({
        lang: 'ru-RU',
        placeholder: 'Введите текст акции',
        height: 300,
        focus: true,
        toolbar: [
            ['style', ['style']],
            ['font', ['bold', 'underline', 'clear']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['table', ['table']],
            ['insert', ['link', 'picture', 'video']],
            ['view', ['fullscreen', 'codeview', 'help']]
        ]
    });
}

/**
 * Function creates allert message when fields in modal are invalid
 * @param text - text of message
 * @param focusField - field to focus
 */
function invalidModalField(text, focusField) {
    document.querySelector('#modal-alert').innerHTML = `<div class="alert alert-danger alert-dismissible fade show" role="alert">
                                                                    <strong>${text}</strong>
                                                                     <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                                                         <span aria-hidden="true">&times;</span>
                                                                     </button>
                                                                </div>`
    focusField.focus()
    window.setTimeout(function () {
        $('.alert').alert('close');
    }, 3000)
}

function successActionMainPage(inputField, text, messageStatus) {
    let successMessage = `<div class="alert text-center alert-success alert-dismissible" role="alert">
                            <strong>${text}</strong>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span></button>
                       </div>`
    let alertMessage = `<div class="alert text-center alert-danger alert-dismissible" role="alert">
                            <strong>${text}</strong>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span></button>
                       </div>`
    let message = ''

    if (messageStatus === "success") {
        message = successMessage
    } else if (message === "error") {
        message = alertMessage;
    }

    document.querySelector(`${inputField}`).innerHTML = message
    window.setTimeout(function () {
        $('.alert').alert('close');
    }, 3000)
}