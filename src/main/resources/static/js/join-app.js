import { enableLink, disableLink} from "/js/element-utils.js";

// Threshold in minutes to set a timout for enabling the button
const thresholdMinutes = 15;
const startDateAttribute = "data-start-date";

export default class JoinApp {
  constructor(link) {
    this.link = link;
    this.initalDate = new Date();
    this.startDate = new Date(link.getAttribute(startDateAttribute));

    const isActive = this.startDate <= this.initalDate;

    if (isActive) {
      enableLink(this.link);
    } else {
      disableLink(this.link);

      const startDateDiff = this.startDate - this.initalDate;

      if (startDateDiff < (thresholdMinutes * 60 * 1000)) {
        setTimeout(() => {
          enableLink(this.link);
        }, startDateDiff)
      }
    }
  }
}
