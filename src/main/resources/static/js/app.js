// Loading screen animation
const tl = gsap.timeline() // bind gsap timeline
let initialized = false
let table

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

/**
 * Toggle a loading class for an element
 * @param {Object} element DOM Element
 * @param {Object[]} functions
 */
function animateLoading(element) {
	const preHTML = element.html()
	
	function begin()
	{
		element.empty()
		element.addClass('element-loading')
		element.append('<i></i><i></i><i></i>')
	}
	
	function done()
	{
		element.empty()
		element.removeClass('element-loading')
		element.html(preHTML)
	}

	return {
		begin,
		done
	}	
}

/**
 * Get wines data based on color param via AJAX
 * @param {String} color Color param {red | white} (optional)
 * @param {Array} fields String of field arrays (optional)
 * @param {Object} loadingBtnEl Button element with loading animation
 */
function getForTables(color, fields, loadingBtnEl) {
	if (fields) {
		fields = fields.filter((value, index, array) => array.indexOf(value) === index)
	}

	$.ajax({
		url: '/api/wines',
		type: 'get',
		data: {
			color: color ? color : CURR_WINE_COLOR,
			fields: fields ? fields.join(",") : null
		},
		success(res) {
			if (loadingBtnEl) {
				loadingBtnEl.done()
				MicroModal.close('filter-modal')
			}

			createData(res, fields)
		}
	})
	
	/**
	 * Append and create relevant table for wines
	 * @param {Array} data Array of wine objects
	 * @param {Array} fields Array of fields string
	 */
	function createData(data, fields) {
		if (table) {
			table.clear()
			table.destroy()
		}
		
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
													<td>${wine.pH}</td>
													<td>${wine.sulphates}</td>
													<td>${wine.alcohol}</td>
													<td>${wine.quality}</td>
											</tr>`)
			$('.table').append(html)
		})
	
		table = new DataTable('#table', {
			scrollX: true,
			layout: {
				topEnd: {
					search: {
						placeholder: 'Search here'
					}
				}
			}
		})

		// Add little sort buttons to each table head col
		// $('.dt-column-order:not(:first)').empty().append(`
		$('.dt-column-order').empty().append(`
			<i class="material-symbols-rounded up">keyboard_arrow_up</i>
			<i class="material-symbols-rounded down">keyboard_arrow_down</i>
		`)

		// Add the filter modal trigger button
		$('.dt-layout-cell.dt-layout-end:not(:last)').prepend(`
			<div data-micromodal-trigger="filter-modal" class="filter-modal-trigger"><i class="material-symbols-rounded">filter_alt</i> Filter</div>
		`)

		table.draw()

		// Hide all the fields that aren't in the filter
		if (fields) {
			$('.dt-scroll-head thead th').each((i, th) => {
				const dID = $(th).data('id')

				if (!fields.includes(dID)) {
					table.column($(th).data('dt-column')).visible(false)
				}
			})
		}

		bindFilterBtn()
	}
}

// Handle click event for the filter button
function bindFilterBtn()
{
	const filterBtnEl = $('.js-filter')

	MicroModal.init({
		disableScore: true
	})

	filterBtnEl.unbind().click(submitModalForm)
}

/**
 * Send the relevant fields via AJAX
 */
function submitModalForm()
{
	const checkedFieldBoxes = $('.filter-modal input:checkbox:checked')
	const loadingBtnEl = animateLoading($('.js-filter'))
	let checkedFieldsArr = []

	checkedFieldBoxes.each((index, field) => {
		const f = $(field).val()

		checkedFieldsArr.push('color') // hard-code to fetch color also
		checkedFieldsArr.push(f)
	})

	loadingBtnEl.begin()
	getForTables(null, checkedFieldsArr, loadingBtnEl)
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
	if (initialized) return

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

	initialized = true // play animations only for page load
}