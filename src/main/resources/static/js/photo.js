class Photo {
    constructor(id) {
        this.wraper = id;
        this.page = 1;
        this.hasNext = true;
        this.picList = [];
    }

    loadNextPage() {
        $.get("./pic/list/5/" + this.page, (data, status) => {
            if (status === 'success') {
                if (data.length > 0) {
                    this.page++
                    for (let i = 0; i < data.length; i++) {
                        let time = data[i]['uploadTime'];
                        let divId = "#" + time;
                        $(this.wraper).append(`<div id="${time}"></div>`)
                        $(divId).append(`<p>${this.convertTime(time)}</p>`)
                        for (let j = 0; j < data[i]['names'].length; j++) {
                            let name = data[i]['names'][j]
                            let prv = './pic/preview/' + name;
                            $(divId).append(`<img src="${prv}" alt="${name}" class='pre-pic' onclick='showImg("${name}")'>`)
                            this.picList.push(name)
                        }
                        $(divId).append(`<p>${data[i]['desc']}</p>`)
                    }
                    this.hasNext = true;
                } else {
                    this.hasNext = false;
                }
            }
        })
    }

    convertTime(timeMills) {
        let date = new Date(timeMills);
        let Y = date.getFullYear() + '-';
        let M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        let D = date.getDate() + ' ';
        let h = date.getHours() + ':';
        let m = date.getMinutes() + ':';
        let s = date.getSeconds();
        return Y+M+D+h+m+s;
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

function showImg(name) {
    let src = './pic/src/' + name;
    $(".pop-window").show()
    let img = $("#src-img")
    img.attr("src", src)
    img.attr("alt", name)
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

function lastPic() {
    let img = $("#src-img")
    let index = photo.picList.indexOf(img.attr("alt")) - 1;
    if (index < 0) {
        return
    }
    let lastName = photo.picList[index];
    showImg(lastName)
    console.log(photo.picList)
    console.log()
}

function nextPic() {
    let img = $("#src-img")
    let index = photo.picList.indexOf(img.attr("alt")) + 1;
    if (index >= photo.picList.length) {
        return
    }
    let nextName = photo.picList[index];
    showImg(nextName)
}


$(document).keydown(function (event) {
    if (event.keyCode === 37) {
        lastPic()
    } else if (event.keyCode === 39) {
        nextPic()
    } else if (event.keyCode === 27) {
        closePop()
    }
})



