class Photo {
    constructor(id) {
        this.wraper = id;
        this.page = 1;
        this.hasNext = true;
    }

    loadNextPage() {
        $.get("./pic/list/5/" + this.page, (data, status) => {
            if (status === 'success') {
                if (data.length > 0) {
                    this.page++
                    for (let i = 0; i < data.length; i++) {
                        let name = data[i].names[0]
                        let prv = './pic/preview/' + name;
                        $(this.wraper).append(`<li><img src="${prv}" alt="${name}" class='pre-pic' onclick='showImg("${name}")'></li>`)
                    }
                    this.hasNext = true;
                } else {
                    this.hasNext = false;
                }
            }
        })
    }
}

let photo = new Photo("#image-list")
photo.loadNextPage()
$(window).scroll(function () {
    if($(window).scrollTop() + $(window).height() >= $(document).height() - 10 && photo.hasNext){
        photo.loadNextPage()
    }
})

$(window).bind("beforeunload", function () {
    $(window).scrollTop(0)
})

$("#close").click(function () {
    console.log("close")
    $(".pop-window").hide()
})

function showImg(name) {
    let src = './pic/src/' + name;
    $(".pop-window").show()
    $("#src-img").attr("src", src)
    $('body').css({
        "overflow":"hidden"
    });
}

function closePop() {
    console.log("close")
    $(".pop-window").hide()
    $('body').css({
        "overflow":"auto"
    });
}

