/* -------------------------------------------
project:	proposedsociety.com
Version : 1.01 :: 16 May 2013
Created By Bhanu Pratap singh
Author : www.bhanubais.com
------------------------------------------- */

$(document).ready(function(){
	// Global js path/url
	var jsPath = '/assets/js/';
	
	// Auto Scrolling
	/*(function($){
		$.fn.autoScroll = function(){
			var first = $(this).children().eq(0);
			$(this).animate({
				scrollTop: 120
			}, 'slow', 'swing', function(){
				$(this).append(first);
				$(this).scrollTop(0);
			});
		}
	})(jQuery);
	window.setInterval(function(){
		$('.auto_scrolling_1').autoScroll();
		$('.auto_scrolling_2').autoScroll();
	}, 5000);*/

	// Scroller
	$.getScript(jsPath+'jquery.nanoscroller.min.js', function(){
		$('.nano').nanoScroller({scroll: 'top', preventPageScrolling: true });
		$('.nano_member').nanoScroller({scroll: 'top', preventPageScrolling: true });
	});
	
	// Marquee Text
	var para = $('.marquee').children('p').eq(0).clone(true);
	$('.marquee').append(para);
	paraWidth = para.width();
	function marquee(){
		$('.marquee').animate({marginLeft: -paraWidth}, 20000, 'linear', function(){
			$(this).children('p').eq(0).appendTo(this);
			$(this).css({marginLeft: 0});
		});
	}
	marquee();
	window.setInterval(marquee, 20000);

	// Dropdown
	$.getScript(jsPath+'bootstrap-dropdown.js', function(){
		$('.dropdown-toggle').dropdown();
	});
	
	// Tabs
	$.getScript(jsPath+'bootstrap-tab.js', function(){
		$('#vTab a').click(function (e) {
			e.preventDefault();
			$(this).tab('show');
		})
	});
	
	// Alerts
	$.getScript(jsPath+'bootstrap-alert.js', function(){
		$(".alert").alert();
	});

	// Tooltip on form
	$.getScript(jsPath+'bootstrap-tooltip.js', function(){
		$.getScript(jsPath+'bootstrap-popover.js', function(){
			$('#form_steps').find('a').popover({
				'trigger': 'hover'
			});

			$("#new_visitor #mobile").tooltip({trigger: 'focus', placement: 'top'});
			$("#new_visitor #email").tooltip({trigger: 'focus', placement: 'top'});
		})
	});

	// Auto insert new row in form
	var next = 1;
	$('.autoincrement').on('click', function(){
		next++;
		var member = 'Member' + next;
		var elem = $(this).closest('.field');
		elem.clone(true).insertAfter(elem).children('label').text(member);
		elem.find('.input-append').children('input').appendTo(elem.find('.controls'));
		elem.find('.input-append').remove();

	})

	// Ajax based table in Dashboard using fancybox
	$.getScript(jsPath+'fancyBox2/lib/jquery.mousewheel-3.0.6.pack.js', function(){
		$.getScript(jsPath+'fancyBox2/source/jquery.fancybox.pack.js?v=2.1.5', function(){
			$.getScript(jsPath+'fancyBox2/source/helpers/jquery.fancybox-buttons.js?v=2.1.5', function(){

				$('.call_to_ajax').fancybox({
					maxWidth	: 500,
					maxHeight	: 350,
					fitToView	: false,
					width		: '70%',
					height		: '70%',
					autoSize	: false,
					closeClick	: false,
					openEffect	: 'none',
					closeEffect	: 'none'
				});

				$(".fancybox-buttons").fancybox({
					prevEffect		: 'none',
					nextEffect		: 'none',
					closeBtn		: false,
					helpers		: {
						title	: { type : 'inside' },
						buttons	: {}
					}
				});
				
			})
		})
	})

	// Temprary member form actions
	//$(document).on('click', '.btn-success', function(e){
	//	e.preventDefault();
	//	$('#block_screen').fadeOut('slow');
	//});

	
	// Intro Text
	$(document).on('click', 'a.know_more', function(e){
		e.preventDefault();
		$(this).prev('.content').slideDown('slow');
		$(this).text('hide').addClass('hide_this');
	});
	$(document).on('click', 'a.hide_this', function(e){
		e.preventDefault();
		$(this).prev('.content').slideUp('slow');
		$(this).text('Know More').removeClass('hide_this');
	});
	
	
});
