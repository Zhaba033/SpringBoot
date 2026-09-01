const listEls = document.querySelectorAll('#quicksearch li');
const input = document.getElementById('search');
const display = listEls[0].style.display;

input.addEventListener('input', () =>
	{
		for (const el of listEls) {
			if (!el.textContent.toLowerCase().includes(input.value.toLowerCase()) && input.value !== '') {
				el.style = "display: none;"
			} else {
				el.style = "display: " + display + ";"
			}
		}
	}
)