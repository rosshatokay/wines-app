// Loading screen animation
const tl = gsap.timeline() // bind gsap timeline

$('.loading-screen .fg').css('width', `${getRandomInt(20, 60)}%`)

/**
 * Return a random number between min | max
 * @param {Integer} min Minimum int
 * @param {Integer} max Maximum int
 */
function getRandomInt(min, max) {
	const minCeiled = Math.ceil(min);
  const maxFloored = Math.floor(max);
  return Math.floor(Math.random() * (maxFloored - minCeiled) + minCeiled)
}

function getForTables() {
	$.ajax({
		url: '/api/wines',
		type: 'get',
		data: {
			// color: "red"
		},
		success(res) {
			createData(res.slice(0, 10))
		}
	})
	
	function createData(data) {
		$('.total-results').text(`Showing ${data.length} wines`)

		data.forEach((wine, i) => {
			const html = $(`<tr>
													<td>
															<div class="flex items-center" style="gap: 16px">
																	<div class="icon ${wine.color}">${wine.color[0].toUpperCase()}</div>
																	<span>${wine.color == 'red' ? 'Red wine' : 'White wine'}</span>
															</div>
													</td>
													<td>${wine.fixedAcidity}</td>
													<td>${wine.volatileAcidity}</td>
													<td>${wine.citricAcid}</td>
													<td>${wine.residualSugar}</td>
													<td>${wine.chlorides}</td>
													<td>${wine.freeSulfurDioxide}</td>
													<td>${wine.totalSulfurDioxide}</td>
													<td>${wine.density}</td>
													<td>${wine.ph}</td>
													<td>${wine.sulphates}</td>
													<td>${wine.alcohol}</td>
													<td>${wine.quality}</td>
											</tr>`)
			$('.table').append(html)
		})
	
		const table = new DataTable('#table', {
			scrollX: true,
			layout: {
				topEnd: {
					search: {
						placeholder: 'Search here'
					}
				}
			}
		})

		$('.dt-column-order:not(:first)').append(`
			<i class="material-symbols-rounded up">keyboard_arrow_up</i>
			<i class="material-symbols-rounded down">keyboard_arrow_down</i>
		`)

		$('.dt-layout-cell.dt-layout-end:not(:last)').prepend(`
			<div class="js-filter" data-micromodal-trigger="filter-modal"><i class="material-symbols-rounded">filter_alt</i> Filter</div>
		`)

		table.draw()

		bindFilterBtn()
	}
}

// Handle click event for the filter button
function bindFilterBtn()
{
	const btn = $('.js-filter')

	MicroModal.init({
		disableScore: true
	})
}

// Animate loading screen before loading location path
function loadingScreenPreLocation() {
	const link = $('a')

	link.unbind().click(function(e) {
		e.preventDefault()

		$('.loading-screen--inner .fg').css('width', 0)

		tl.to('.loading-screen', {
			duration: 0.5,
			ease: 'circ',
			y: 0
		}).to('.loading-screen--inner', {
			opacity: 1,
			duration: 0.3,
			ease: 'circ'
		})

		setTimeout(() => {
			window.location = $(this).attr('href')
		}, 800)
	})
}

function initLanding() {
	$.ajax({
		url: "/api/wines/split-by-color",
		type: "get",
		success(res) {
				$('.categories--box.all .badge').text(`${res.redCount + res.whiteCount} wines`)
				$('.categories--box.red .badge').text(`${res.redCount} wines`)
				$('.categories--box.white .badge').text(`${res.whiteCount} wines`)
		}
	})
}

loadingScreenPreLocation()

$(document).ajaxComplete(function () {
	setTimeout(() => {
		$('.loading-screen .fg').css('width', '100%')
	}, 300)
	setTimeout(() => {
		doneLoading()
	}, 1000)
})

// After page is done loading, animate the relevant elements
function doneLoading() {
	tl.to('.loading-screen--inner', {
		opacity: 0,
		ease: 'circ',
		duration: 0.6
	}).to('.loading-screen', {
		duration: 0.6,
		ease: 'circ',
		y: "-100%"
	}).to('.gs--up', {
		opacity: 1,
		duration: 0.8,
		y: 0,
		stagger: 0.08,
		ease: 'circ'
	}).to('.loading-screen', {
		duration: 0,
		y: "100%"
	})

	tl.play()
}