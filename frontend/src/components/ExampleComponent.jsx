import Button from "react-bootstrap/Button";
import React from "react";

function ExampleComponent({ callback, children }) {
  return (
    <div>
      <div className="">{children}</div>
      <Button onClick={callback}>Bootstrap Button</Button>
    </div>
  );
}

export default ExampleComponent;
