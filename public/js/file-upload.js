(function( $ ) {
	$.fn.fileUpload = function(options) {
		var settings = $.extend({
			uploadUrl: '/files/upload',
			method: 'POST',
			onUpload: function() {},
			onRemove: function() {},
			onUploadError: 'Failed to upload file',
			beforeUpload: function(file) { return true; }
		}, options);

		return this.each(function () {
			// Initialize the component with underlying controls.
			$(this).append('<input id="file-input" type="file" style="display: none"/>');
			$(this).append('<input type="hidden"/>');
			$(this).append('<input type="text" placeholder ="Select file to upload" disabled="true" size="10"/>');
			$(this).append('<button id="browse">Browse</button>');
			$(this).append('<button id="cancel" style="display: none;">Cancel</button>');
			$(this).append('<button id="upload">Upload</button>');
			$(this).append('<button id="remove" style="display: none;">Remove</button>');
			$(this).append('<p style="display: none;" class="error"></p>');
			$(this).append('<div progress class="progress progress-striped active" style="display: none;">'
						 + '	<div bar class="bar"></div>'
						 + '</div>');

			var control = $(this);
			var inputFile = $("input[type=file]", control);
			var inputHidden = $("input[type=hidden]", control);
			var inputText = $("input[type=text]", control);
			var browse = $("button#browse", control);
			var upload = $("button#upload", control);
			var cancel = $("button#cancel", control);
			var remove = $("button#remove", control);
			var progress = $("div[progress]", control);
			var progressBar = $("div[bar]", progress);
			var errorMsg = $("p", control);
			var cancelUpload;

			if (settings.buttonClass) {
				$("button", control).addClass(settings.buttonClass);
			}

			if (settings.inputClass) {
				inputText.addClass(settings.inputClass);
			}

			if (settings.initialValue) {
				inputHidden.val(settings.initialValue);
				
				if (settings.initialName) {
					inputText.val(settings.initialName);
				}

				browse.hide();
				upload.hide();
				remove.show();
			}

			inputFile.change(function () {
				inputText.val(inputFile.get(0).files[0].name);
				errorMsg.text("").hide();
			});

			browse.click(function(event) {
				event.preventDefault();
				inputFile.trigger("click");
			});

			cancel.click(function(event) {
				event.preventDefault();
				cancelUpload.abort();
			});
			
			upload.click(function(event) {
				event.preventDefault();

				errorMsg.text("").hide();

				var files = inputFile.get(0).files;

				if (files && files[0]) {
					if (settings.beforeUpload(files[0])) {
						var data = new FormData();
						data.append("file", files[0]);

						cancelUpload = $.ajax({
							url: "/files/upload",
							type: "POST",
							data: data,
							processData: false,
							contentType: false,
							
							// Customize the XMLHttpRequest object to add event handler for progress tracking.
							xhr: function () {
								var customXhr = $.ajaxSettings.xhr();
								
								if (customXhr.upload) {
									customXhr.upload.addEventListener('progress', function (event) {
										if (event.lengthComputable) {
											if (event.total > 0) {
												var percentage = Math.round(event.loaded / event.total * 100);
												progressBar.width(percentage + "%");
											}
										}
									} , false);
								}

								return customXhr;
							},
							beforeSend: function() {
								inputText.prop('disabled', true);
								upload.prop('disabled', true);
								browse.hide();
								progress.show();
								cancel.show();
							},
							success: function(data) {
								upload.hide();
								remove.show();
								inputHidden.val(data);
								settings.onUpload(data);	
							},
							error: function() {
								inputText.prop('disabled', false);
								browse.show();
								errorMsg.text(settings.onUploadError);
								errorMsg.show();
							},
							complete: function() {
								upload.prop('disabled', false);
								progress.hide();
								cancel.hide();
								progressBar.width("0");
								
							}
						});
					}
				} else {
					errorMsg.show().text("Please choose a file");
				}
			});

			remove.click(function (event) {
				event.preventDefault();

				progress.show();
				
				var file = inputHidden.val();
				if (file) {
					$.ajax({
						url: "/files/delete/" + file,
						type: "POST",
					});
				}

				inputFile.val('');
				inputHidden.val('');
				inputText.val('');
				browse.show();
				upload.show();
				remove.hide();
				progress.hide();

				settings.onRemove();
			});
		});
	};
}(jQuery));
