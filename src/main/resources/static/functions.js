let subscription = null;
let newQuery = 0;
let template = null

function registerTemplate() {
	template = $("#template").html();
	Mustache.parse(template);
}

function setConnected(connected) {
	const search = $('#submitsearch');
	search.prop('disabled', !connected);
}

function registerSendQueryAndConnect() {
	const socket = new SockJS("/twitter");
	const stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
    });
	$("#search").submit(
			function(event) {
				event.preventDefault();
				if (subscription) {
					subscription.unsubscribe();
				}
				const query = $("#q").val();
				stompClient.send("/app/search", {}, query);
				newQuery = 1;
				subscription = stompClient.subscribe("/queue/search/" + query, function(data) {
					const resultsBlock = $("#resultsBlock");
					if (newQuery) {
                        resultsBlock.empty();
						newQuery = 0;
					}
					const tweet = JSON.parse(data.body);
					resultsBlock.prepend(Mustache.render(template, tweet));
				});
			});
}

$(document).ready(function() {
	registerTemplate();
	registerSendQueryAndConnect();
});
