function Paginate(el, numToShowOnScroll, numOfShownInitially) {
    let currPage = 0;
    let initialPageSize = numOfShownInitially || numToShowOnScroll;
    let toHide = el.filter(':gt(' + (initialPageSize - 1) + ')');
    toHide.addClass("hidden");

    $(window).scroll(function () {
        if ($(document).height() - $(this).height() === Math.ceil($(this).scrollTop())) {
            ++currPage;
            let endIdx = initialPageSize + currPage * numToShowOnScroll;
            let toShow = el.filter(':lt(' + endIdx + '):gt(' + (endIdx - numToShowOnScroll - 1) + ')');
            toShow.removeClass("hidden");
        }

    });
}

Paginate($('div.infinite-scroll'), 3, 9);
