const titleAttribute = "data-details-title";
const descriptionAttribute = "data-details-description";

export default class DetailsModalApp {
  constructor(trigger, modal) {
    this.trigger = trigger;
    this.modal = modal;

    this.title = trigger.getAttribute(titleAttribute);
    this.description = trigger.getAttribute(descriptionAttribute);

    trigger.addEventListener("click", () => this.initModal());
  }

  async initModal() {
    const titleEl = this.modal.querySelector(`[${titleAttribute}]`);
    titleEl.innerText = this.title;

    const descriptionEl = this.modal.querySelector(`[${descriptionAttribute}]`);
    descriptionEl.innerText = this.description;

    const modal = bootstrap.Modal.getOrCreateInstance(this.modal);
    modal.show();
  }
}
