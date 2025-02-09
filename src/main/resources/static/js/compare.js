function Comparer()
{
	function init()
	{
		addCTA()
	}

	/**
	 * Add compare btn if not in the compare page
	*/
	function addCTA() {
		if (COMPARING) {
			getData()
			return
		}

		const modal_cta_el = $(`
			<div class="compare-cta gs--up">
				<a href="/wines/compare" class="compare-cta--btn">
					Compare wines<i class="material-symbols-rounded">compare_arrows</i>
				</a>
			</div>`)
			
		$('body').append(modal_cta_el)

		bindCTA(modal_cta_el)
	}

	// Handle compare btn click
	function bindCTA(btn)
	{
		btn.unbind().click(getData)
	}

	function getData()
	{
		$.ajax({
			url: '/api/multi-query',
			type: 'get',
			success(res) {
				renderData(res)
			}
		})
	}

	/**
	 * Render data from multi-query API call
	 * @param {Object} data Data from response
	 */
	function renderData(data) {
		const wines = data.redWines.concat(data.whiteWines) // merge arrays
		
		wines.forEach((wine) => {
			const html = _createRowHTML(wine)

			if (wine.color == 'red') {
				$('#red-table tbody').append(html)
			} else {
				$('#white-table tbody').append(html)
			}
		})

		let redTable = new DataTable('#red-table', {
			scrollX: true,
			layout: {
				topEnd: {
					search: {
						placeholder: 'Search here'
					}
				}
			}
		})

		let whiteTable = new DataTable('#white-table', {
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
		
		redTable.draw()
		whiteTable.draw()

		console.log(data.redWines.length)
		$('.red-wines-count').text(data.redWines.length)
		$('.white-wines-count').text(data.whiteWines.length)
	}
 
	
	/**
	 * Create a table for wines by type
	 * @param {Object} winee - Wine object
	 * @returns {HTMLElement}
	 */
	function _createRowHTML(wine)
	{
		const html = $(`<tr>
												<td>
														<div class="flex items-center" style="gap: 16px">
																<div class="icon ${wine.color}">${wine.color[0].toUpperCase()}</div>
																<span>${wine.color == 'red' ? 'Red wine' : 'White wine'}</span>
														</div>
												</td>
												<td>${wine.pH}</td>
												<td>${wine.alcohol}</td>
										</tr>`)
		
		return html
	}
	
	return {
		init
	}
}


Comparer().init()