const idAttribute = "data-deletion-id";
const titleAttribute = "data-deletion-title";
const textAttribute = "data-deletion-text";
const confirmAttribute = "data-deletion-confirm";

export default class DeletionModalApp {
  constructor(trigger, modal) {
    this.trigger = trigger;
    this.modal = modal;

    this.meetingId = trigger.getAttribute(idAttribute);
    this.meetingTitle = trigger.getAttribute(titleAttribute);
    this.text = trigger.getAttribute(textAttribute);

    trigger.addEventListener("click", () => this.initModal());
  }

  async initModal() {
    const textEl = this.modal.querySelector(`[${textAttribute}]`);
    textEl.innerText = this.text.replace("{0}", this.meetingTitle);

    const confirmButton = this.modal.querySelector(`[${confirmAttribute}]`);
    confirmButton.setAttribute("href", "/delete/" + this.meetingId);

    const modal = bootstrap.Modal.getOrCreateInstance(this.modal);
    modal.show();
  }
}
