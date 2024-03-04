import { createApp, ref } from "/webjars/vue/dist/vue.esm-browser.prod.js"

const accessDeniedUrl = "/intercept/oauth2/access-denied";
const popUpWindowName = "oAuth2Popup";
const popUpWindowFeatures = "toolbar=no, menubar=no, width=600, height=700, top=100, left=100";

const oauth2AppOptions = {
  setup() {
    // Input reference
    const popupUrlInput = ref(null);

    const popupWindow = ref(null);
    const popupBlocked = ref(false);
    const popupCheckInterval = ref (null);

    return {
      popupUrlInput,
      popupWindow,
      popupBlocked,
      popupCheckInterval,
    }
  },
  computed: {
    popupUrl() {
      return this.popupUrlInput?.value;
    }
  },
  methods: {
    launchPopup() {
      const oauth2Url = this.popupUrlInput?.value;
      if (!oauth2Url) {
        console.error("No oauth2Url. popupUrlInput: ", this.popupUrlInput);
        return;
      }

      this.popupWindow = window.open(oauth2Url, popUpWindowName, popUpWindowFeatures);
      this.checkPopupBlocked(this.popupWindow)
      if (!this.popupBlocked) {
        this.repeatedCheck(true);
      }
    },
    recieveCallbackMessage(messageEvent) {
      this.clearRepeatedCheck();

      const popupLocation = messageEvent.data;

      if(popupLocation.includes("access_denied")) {
        this.accessDenied("callback");
        return;
      }

      window.location.replace(popupLocation);
    },
    checkPopupBlocked(popupWindow) {
      this.popupBlocked = popupWindow == null || popupWindow.closed || typeof popupWindow.closed == "undefined";
    },
    repeatedCheck(isInit) {
      if (isInit) {
        this.clearRepeatedCheck();
        this.popupCheckInterval = setInterval(() => this.repeatedCheck(false), 500);
      }

      if (this.popupWindow?.closed) {
        this.accessDenied();
      }
    },
    clearRepeatedCheck() {
      if (this.popupCheckInterval) {
        clearInterval(this.popupCheckInterval);
      }
    },
    accessDenied() {
      window.location.replace(accessDeniedUrl);
    },
  },
  mounted() {
    window.addEventListener('message',
        messageEvent => this.recieveCallbackMessage(messageEvent), false);

    this.launchPopup();
  },
};

export default class OAuth2App {
  constructor(root) {
    createApp(oauth2AppOptions).mount(root);
  }
}
