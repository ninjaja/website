function Paginate(el, pageSize, initPageSize) {
    let currPage = 0;
    let initialPageSize = initPageSize || pageSize;
    let toHide = el.filter(':gt(' + (initialPageSize - 1) + ')');
    toHide.addClass("hidden");

    $(window).scroll(function () {
        if ($(document).height() - $(this).height() === Math.ceil($(this).scrollTop())) {
            ++currPage;
            let endIdx = initialPageSize + currPage * pageSize;
            let toShow = el.filter(':lt(' + endIdx + '):gt(' + (endIdx - pageSize - 1) + ')');
            toShow.removeClass("hidden");
        }

    });
}

Paginate($('div.infinite-scroll'), 3, 6);
