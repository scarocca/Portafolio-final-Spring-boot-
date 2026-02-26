// En tu archivo scripts.js
document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.card-flip-container').forEach(card => {
        card.addEventListener('click', function() {
            this.classList.toggle('is-flipped');
        });
    });
});