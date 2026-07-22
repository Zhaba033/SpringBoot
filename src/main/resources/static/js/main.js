function update(node) {
  const HEIGHT_LIMIT = calculateHeightLimit(node);
  
  node.style.overflow = "hidden";
  node.style.height = "auto"; 
  let newHeight = node.scrollHeight; 
  
  newHeight = Math.min(newHeight, HEIGHT_LIMIT);
  
  if (node.style.height !== newHeight + "px") {
    smoothHeightTransition(node, newHeight);
  }
}

function smoothHeightTransition(node, newHeight) {
  requestAnimationFrame(() => {
    node.style.height = node.offsetHeight + "px";
    node.offsetHeight; 
    requestAnimationFrame(() => {

      node.style.height = newHeight + "px";
    });
  });
}

function calculateHeightLimit(node) {
  return 60;
}