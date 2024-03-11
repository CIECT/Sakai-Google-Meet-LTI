const disabledClass = "disabled";
const disabledAttribute = "aria-disabled";

export function disableLink(link) {
  link.classList.add(disabledClass);
  link.setAttribute(disabledAttribute, true);
}

export function enableLink(link) {
  link.classList.remove(disabledClass);
  link.setAttribute(disabledAttribute, false);
}
