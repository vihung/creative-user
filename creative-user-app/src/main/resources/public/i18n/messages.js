var messages;

var lang = (navigator.language || navigator.systemLanguage || navigator.userLanguage || 'en').substr(0, 2).toLowerCase();

$(document).ready(function() {
  loadMessages();
  $("message").each(function(i, element) {
    var $element = $(element);
    substituteMessage($element);
    substitutePlaceholder($element);
  });
})

function loadMessages() {
  lang = (navigator.language || navigator.systemLanguage || navigator.userLanguage || 'en').substr(0, 2).toLowerCase();
  $.getJSON("/i18n/messages.json", function(json) {
    messages = json[lang];
  });
}

function message(messageKey) {
  var message= messages[messageKey];
  if(message) {
    return message;
  } else{
    return "MESSAGE_NOT_FOUND[" + messageKey + "]";
  }
}

function substituteMessage($element) {
  console.log("substituteMessage(): Invoked");
  console.log($element);
  var message = $element.attr("data-message");
  console.log(message)
  if (message) {
    $(element).text(message)
  }
}

function substitutePlaceholder($element) {
  console.log("substitutePlaceholder(): Invoked");
  console.log($element);
  var placeholder = $element.attr("data-placeholder");
  console.log(placeholder);
  if (placeholder) {
    $(element).attr('placeholder', placeholder);
  }
}
