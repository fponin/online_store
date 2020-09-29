let productIdFromPath = decodeURI(document.URL.substring(document.URL.lastIndexOf('/') + 1));

/**
 * Get-запрос - если получаем продукт, то вызываем заполняющие страницу функции,
 * иначе "страница не найдена"
 */
fetch("/api/products" + `/${productIdFromPath}`)
    .then(function (response) {
        if (response.status === 200) {
            response.json().then(function (data) {
                fillBreadcrumb(data);
                fillAboutProduct(data);
            })
        } else if (response.status === 404) {
            location.href = "/404";
        }
    });

/**
 * Заполняет навигационную цепочку под хедером страницы
 *
 * @param data продукт из БД по id
 * @returns {Promise<void>}
 */
async function fillBreadcrumb(data) {
    let categories = await fetch("/api/categories/categories").then(response => response.json());
    let categoriesWithLatin = await fetch("/api/categories").then(response => response.json());

    let breadcr = document.getElementById('categoriesBreadcrumb');
    let categoryOfProduct;
    let subcategoryOfProduct;

    // определение категории для продукта
    top:
        for (let i in categories) {
            let prodInCategory = categories[i].products;
            for (let j in prodInCategory) {
                if (prodInCategory[j].id == data.id) {
                    categoryOfProduct = categories[i].superCategory;
                    subcategoryOfProduct = categories[i].category;
                    break top;
                }
            }
        }

    // формирование навигационной цепочки
    $(breadcr).append(`<li class="breadcrumb-item"><a href="/">Главная</a></li>`);

    // поиск наименования категории на латинице
    if (categoryOfProduct != undefined) {
        let subcategoryOfProductInLatin = categoriesWithLatin[categoryOfProduct][subcategoryOfProduct];
        $(breadcr).append(`<li class="breadcrumb-item"><a>${categoryOfProduct}</a></li>`);
        $(breadcr).append(`<li class="breadcrumb-item"><a href="/categories/${subcategoryOfProductInLatin}">${subcategoryOfProduct}</a></li>`);
    }
    $(breadcr).append(`<li class="breadcrumb-item"><a>${data.product}</a></li>`);
}

/**
 * Заполняет информацию о продукте
 *
 * @param data продукт из БД по id
 * @returns {Promise<void>}
 */
async function fillAboutProduct(data) {
    let productName = document.getElementById('productName');
    $(productName).append(`<br><h2 class="font-weight-normal">${data.product}</h2>`);
    $(productName).append(`<br><h5 class="font-weight-normal">Оценка товара: ${data.rating} из 5</h5>`);

    // заполнение вкладки с описанием продукта
    let description = document.getElementById('text-description');
    if (data.descriptions == null) {
        $(description).append("Извините, описание пока не заполнено.");
    } else {
        $(description).append(`${data.descriptions.information}`);
    }

    // заполнение вкладки с характеристиками продукта
    let specifications = document.getElementById('text-specifications');
    for (let key in data.descriptions) {
        if (key != "id" && key != "information") {
            let content = `<tr><td class="font-weight-bold">${key}</td>
                           <td>${data.descriptions[key]}</td>
                       </tr>`;
            $(specifications).append(content);
        }
    }
}