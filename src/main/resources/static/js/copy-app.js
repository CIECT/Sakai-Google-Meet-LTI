// Copy to clipboard app to be initated on a button passed in constructor

const resetTime = 1500;

export default class CopyApp {
  constructor(button) {
    this.button = button;
    this.buttonTitle = button.getAttribute("title");
    this.popoverContent = button.getAttribute("data-popover-content");
    this.copyUrl = button.getAttribute("data-url");

    this.defaultIcon = button.querySelector("[data-icon-default]");
    this.copiedIcon = button.querySelector("[data-icon-copied]");

    button.addEventListener("click", () => this.copyLink());
  }

  async copyLink() {
    try {
      await navigator.clipboard.writeText(this.copyUrl);
    } catch (error) {
      console.error(error.message);
      return false;
    }

    // Is there a timepout? Someone might spam the button
    if (this.resetTimeout) {
      // Clear existing timeout
      clearTimeout(this.resetTimeout);
    } else {
      // Only show the button, when the timeout ran out
      this.#toggleIcon(true);
      this.#togglePopover(true);
    }

    this.resetTimeout = setTimeout(() => {
      this.#toggleIcon(false);
      this.#togglePopover(false);
      this.resetTimeout = null;
    }, resetTime);

    return true;
  }
  
  #toggleIcon(showLinkCopied) {
    if (showLinkCopied) {
      this.defaultIcon.classList.add("d-none");
      this.copiedIcon.classList.remove("d-none");
    } else {
      this.defaultIcon.classList.remove("d-none");
      this.copiedIcon.classList.add("d-none");
    }
  }

  #togglePopover(showLinkCopied) {
    const popover = bootstrap.Popover.getOrCreateInstance(this.button);
    if (showLinkCopied) {
      popover.setContent({ '.popover-body': this.popoverContent })
      popover.show();
    } else {
      popover.hide();
      this.button.setAttribute("title", this.buttonTitle);
    }
  }
}
